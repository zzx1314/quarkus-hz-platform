package org.huazhi.oauth.service;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.huazhi.system.sysuser.entity.dto.SysUserDto;
import org.huazhi.system.sysuser.service.SysUserService;
import org.huazhi.util.CommonConstants;
import org.huazhi.util.R;
import org.huazhi.util.RedisUtil;

import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class AuthService {

    @Inject
    SysUserService userService;

    @Inject
    RedisUtil redisUtil;

    @Inject
    JWTParser jwtParser;

    public Response authenticate(String grantType, String username, String password, String clientId,
            String clientSecret) {
        if ("password".equals(grantType)) {
            R<SysUserDto> checkResut = userService.authenticate(username, password);
            if (checkResut.getCode() != CommonConstants.SUCCESS) {
                return Response.status(401)
                        .entity(checkResut)
                        .build();
            }
            String accessToken = generateToken(username, checkResut.getData());
            String refreshToken = generateRefreshToken(checkResut.getData());
            return createTokenResponse(accessToken, refreshToken, checkResut.getData());

        } else if ("client_credentials".equals(grantType)) {
            if (!CommonConstants.VALID_CLIENT_ID.equals(clientId)
                    || !CommonConstants.VALID_CLIENT_SECRET.equals(clientSecret)) {
                return Response.status(401)
                        .entity(Map.of("error", "invalid_client", "error_description", "Invalid client credentials"))
                        .build();
            }
            String accessToken = generateToken(clientId, null);
            return createTokenResponse(accessToken, null, null);

        } else {
            return Response.status(400)
                    .entity(Map.of("error", "unsupported_grant_type"))
                    .build();
        }
    }

    public Response refreshAccessToken(String refreshToken) {
        String key = "refresh_token:" + refreshToken;
        if (!redisUtil.exists(key)) {
            throw new RuntimeException("Refresh token is invalid or expired");
        }

        SysUserDto userDto = redisUtil.getObject(key, SysUserDto.class);
        // 生成新的 Access Token
        String jwt = generateToken(userDto.getUsername(), userDto);

        return createTokenResponse(jwt, refreshToken, userDto);
    }

    public R<Object> logout(String accessToken) {
        String key = "access_token:" + accessToken;
        if (!redisUtil.exists(key)) {
            return R.failed(null,"Access token is invalid or expired");
        }
        redisUtil.del(key);
        return R.ok("Logout successful");
    }

    public R<Void> checkToken(String accessToken) {
        String key = "access_token:" + accessToken;
        if (!redisUtil.exists(key)) {
            return R.failed("Access token is invalid or expired");
        }
        return R.ok();
    }

    private Response createTokenResponse(String jwt, String refreshToken, SysUserDto userDto) {
        ZonedDateTime expirationTime = ZonedDateTime.now().plusSeconds(CommonConstants.EXPIRES_IN);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String expString = expirationTime.format(formatter);

        Map<String, Object> tokenResponse = Map.ofEntries(
                Map.entry("access_token", Objects.requireNonNullElse(jwt, "")),
                Map.entry("refresh_token", Objects.requireNonNullElse(refreshToken, "")),
                Map.entry("token_type", "Bearer"),
                Map.entry("exp", expString),
                Map.entry("expires_in", CommonConstants.EXPIRES_IN),
                Map.entry("username", Objects.requireNonNullElse(userDto.getUsername(), "")),
                Map.entry("permissions", Objects.requireNonNullElse(userDto.getPermissions(), List.of())),
                Map.entry("user_id", Objects.requireNonNullElse(userDto.getId(), 0)),
                Map.entry("roles", Objects.requireNonNullElse(userDto.getRoleIdList(), List.of())));
        return Response.ok(tokenResponse).build();
    }

    private String generateToken(String subject, SysUserDto userDto) {
        String jwt = Jwt.claims()
                .issuer("https://quarkus-oauth2-server")
                .subject(subject)
                .groups(new HashSet<>(Arrays.asList("User", "Admin")))
                .claim("role", userDto != null ? userDto.getRoleIdList() : null)
                .claim("userId", userDto.getId())
                .expiresAt(Instant.now().plusSeconds(CommonConstants.EXPIRES_IN))
                .sign();

        redisUtil.set("access_token:" + jwt, jwt, CommonConstants.EXPIRES_IN);
        return jwt;
    }

    public String generateRefreshToken(SysUserDto userDto) {
        String refreshToken = UUID.randomUUID().toString();
        String key = "refresh_token:" + refreshToken;
        redisUtil.setObject(key, userDto, CommonConstants.REFRESH_EXPIRES_IN);
        return refreshToken;
    }

    public Response checkTokenInfo(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.status(401)
                    .entity(R.failed("UNAUTHORIZED", "Missing or invalid Authorization header"))
                    .build();
        }

        String token = authHeader.substring(7);

        String key = "access_token:" + token;
        // 检查 token 是否存在于 Redis
        if (!redisUtil.exists(key)) {
            return Response.status(401)
                    .entity(R.failed("UNAUTHORIZED", "Invalid access token"))
                    .build();
        }
        try {
            JsonWebToken jwt = jwtParser.parse(token);
            // 额外检查 claims（可选）
            String issuer = jwt.getIssuer();
            if (!"https://quarkus-oauth2-server".equals(issuer)) {
                return Response.status(401)
                        .entity(R.failed("UNAUTHORIZED", "Invalid issuer"))
                        .build();
            }
            // 验证成功：返回 Token 信息或业务响应
            return Response.ok()
                    .entity(R.ok(Map.of("userId", jwt.getSubject(), "exp", jwt.getExpirationTime()), "Token valid"))
                    .build();
        } catch (ParseException e) {
            // 捕获验证失败（e.g., 过期、签名无效）
            String message = switch (e.getCause().getMessage()) {
                case "Token is expired" -> "Token expired";
                case "Signature could not be verified" -> "Invalid signature";
                default -> "Invalid token: " + e.getMessage();
            };
            return Response.status(401)
                    .entity(R.failed("UNAUTHORIZED", message))
                    .build();
        }
    }

}

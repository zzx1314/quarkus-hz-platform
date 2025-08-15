package org.hzai.oauth.controller;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.hzai.system.sysuser.entity.dto.SysUserDto;
import org.hzai.system.sysuser.service.SysUserService;
import org.hzai.util.CommonConstants;
import org.hzai.util.R;

import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/token")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
public class AuthController {
    @Inject
    SysUserService userService;

    private static final String VALID_CLIENT_ID = "myclient";
    private static final String VALID_CLIENT_SECRET = "mysecret";

    private static final long EXPIRES_IN = 3600L;

    @POST
    public Response token(@FormParam("grant_type") String grantType,
            @FormParam("username") String username,
            @FormParam("password") String password,
            @FormParam("client_id") String clientId,
            @FormParam("client_secret") String clientSecret) {

        if ("password".equals(grantType)) {
            R<SysUserDto> checkResut = userService.authenticate(username, password);
            if (checkResut.getCode() != CommonConstants.SUCCESS) {
                return Response.status(401)
                        .entity(checkResut)
                        .build();
            }
            String jwt = generateToken(username, "user");
            return createTokenResponse(jwt, checkResut.getData());

        } else if ("client_credentials".equals(grantType)) {
            if (!VALID_CLIENT_ID.equals(clientId) || !VALID_CLIENT_SECRET.equals(clientSecret)) {
                return Response.status(401)
                        .entity(Map.of("error", "invalid_client", "error_description", "Invalid client credentials"))
                        .build();
            }
            String jwt = generateToken(clientId, "client");
            return createTokenResponse(jwt, null);

        } else {
            return Response.status(400)
                    .entity(Map.of("error", "unsupported_grant_type"))
                    .build();
        }
    }

    private String generateToken(String subject, String role) {
        return Jwt.claims()
                .issuer("https://quarkus-oauth2-server")
                .subject(subject)
                .groups(new HashSet<>(Arrays.asList("User", "Admin")))
                .claim("role", role)
                .expiresAt(Instant.now().plusSeconds(3600))
                .sign();
    }

    private Response createTokenResponse(String jwt, SysUserDto userDto) {
        ZonedDateTime expirationTime = ZonedDateTime.now().plusSeconds(EXPIRES_IN);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String expString = expirationTime.format(formatter);

        Map<String, Object> tokenResponse = Map.ofEntries(
                Map.entry("access_token", Objects.requireNonNullElse(jwt, "")),
                Map.entry("refresh_token", Objects.requireNonNullElse(jwt, "")),
                Map.entry("token_type", "Bearer"),
                Map.entry("exp", expString),
                Map.entry("expires_in", EXPIRES_IN),
                Map.entry("username", Objects.requireNonNullElse(userDto.getUsername(), "")),
                Map.entry("permissions", Objects.requireNonNullElse(userDto.getPermissions(), List.of())),
                Map.entry("user_id", Objects.requireNonNullElse(userDto.getId(), 0)),
                Map.entry("roles", Objects.requireNonNullElse(userDto.getRoles(), List.of())));

        return Response.ok(tokenResponse).build();
    }
}

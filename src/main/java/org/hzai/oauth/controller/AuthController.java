package org.hzai.oauth.controller;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

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
        String expString = expirationTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        Map<String, Object> tokenResponse = Map.of(
                "access_token", jwt,
                "refresh_token", jwt,
                "token_type", "Bearer",
                "exp", expString,
                "expires_in", EXPIRES_IN,
                "username", userDto.getUsername(),
                "permissions", userDto.getPermissions(),
                "user_id", userDto.getId(),
                "roles", userDto.getRoles()
                );
        return Response.ok(tokenResponse).build();
    }
}

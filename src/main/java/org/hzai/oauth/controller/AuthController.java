package org.hzai.oauth.controller;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import org.hzai.system.sysuser.service.SysUserService;

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

    @POST
    public Response token(@FormParam("grant_type") String grantType,
            @FormParam("username") String username,
            @FormParam("password") String password,
            @FormParam("client_id") String clientId,
            @FormParam("client_secret") String clientSecret) {

        if ("password".equals(grantType)) {
            if (!userService.authenticate(username, password)) {
                return Response.status(401)
                        .entity(Map.of("error", "invalid_grant", "error_description", "Invalid credentials"))
                        .build();
            }
            String jwt = generateToken(username, "user");
            return createTokenResponse(jwt);

        } else if ("client_credentials".equals(grantType)) {
            if (!VALID_CLIENT_ID.equals(clientId) || !VALID_CLIENT_SECRET.equals(clientSecret)) {
                return Response.status(401)
                        .entity(Map.of("error", "invalid_client", "error_description", "Invalid client credentials"))
                        .build();
            }
            String jwt = generateToken(clientId, "client");
            return createTokenResponse(jwt);

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

    private Response createTokenResponse(String jwt) {
        Map<String, Object> tokenResponse = Map.of(
                "access_token", jwt,
                "token_type", "Bearer",
                "expires_in", 3600);
        return Response.ok(tokenResponse).build();
    }
}

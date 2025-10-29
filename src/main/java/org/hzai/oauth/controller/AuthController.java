package org.hzai.oauth.controller;

import java.util.Map;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.hzai.oauth.service.AuthService;
import org.hzai.util.R;

import io.smallrye.jwt.auth.principal.JWTParser;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import io.smallrye.jwt.auth.principal.ParseException;

@Path("/token")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
public class AuthController {
    @Inject
    public AuthService authService;

    @Context
    HttpHeaders headers;

    @Inject
    JWTParser jwtParser;

    @POST
    public Response token(@FormParam("grant_type") String grantType,
            @FormParam("username") String username,
            @FormParam("password") String password,
            @FormParam("client_id") String clientId,
            @FormParam("client_secret") String clientSecret) {
        return authService.authenticate(grantType, username, password, clientId, clientSecret);
    }

    @GET
    @Path("/logout")
    public R<Object> logout() {
        String authHeader = headers.getHeaderString("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return authService.logout(token);
        }
        return R.failed("Unauthorized");
    }

    @POST
    @Path("/refresh/{refreshToken}")
    public Response refreshToken(@PathParam("refreshToken") String refreshToken) {
        return authService.refreshAccessToken(refreshToken);
    }

    @GET
    @Path("/check_token")
    public Response checkToken(@HeaderParam("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.status(401)
                    .entity(R.failed("UNAUTHORIZED", "Missing or invalid Authorization header"))
                    .build();
        }

        String token = authHeader.substring(7);

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
                    .entity(R.ok(Map.of("userId", jwt.getSubject(), "exp", jwt.getExpirationTime()),"Token valid"))
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

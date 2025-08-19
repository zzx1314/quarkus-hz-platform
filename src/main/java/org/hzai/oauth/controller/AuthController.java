package org.hzai.oauth.controller;

import org.hzai.oauth.service.AuthService;
import org.hzai.util.R;

import io.quarkus.websockets.next.PathParam;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/token")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
public class AuthController {
    @Inject
    public AuthService authService;

    @Context
    HttpHeaders headers;

    @POST
    public Response token(@FormParam("grant_type") String grantType,
            @FormParam("username") String username,
            @FormParam("password") String password,
            @FormParam("client_id") String clientId,
            @FormParam("client_secret") String clientSecret) {
        return authService.authenticate(grantType, username, password, clientId, clientSecret);
    }

    @POST
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
    public R<Object> checkToken() {
        String authHeader = headers.getHeaderString("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return authService.checkToken(token);
        }
        return R.failed("Unauthorized");
    }

}

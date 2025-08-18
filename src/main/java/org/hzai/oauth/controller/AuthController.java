package org.hzai.oauth.controller;


import org.hzai.oauth.service.AuthService;

import io.quarkus.websockets.next.PathParam;
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
    public AuthService authService;

    @POST
    public Response token(@FormParam("grant_type") String grantType,
            @FormParam("username") String username,
            @FormParam("password") String password,
            @FormParam("client_id") String clientId,
            @FormParam("client_secret") String clientSecret) {
                return authService.authenticate(grantType, username, password, clientId, clientSecret);
       
    }


    @POST
    @Path("/refresh/{refreshToken}")
    public Response refreshToken(@PathParam("refreshToken") String refreshToken) {
        return authService.refreshAccessToken(refreshToken);
    }
    
}

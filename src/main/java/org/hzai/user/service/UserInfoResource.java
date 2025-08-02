package org.hzai.user.service;

import java.util.Map;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/userinfo")
@Produces(MediaType.APPLICATION_JSON)
public class UserInfoResource {

    @Inject
    SecurityIdentity identity;

    @GET
    public Map<String, Object> getUserInfo() {
        return Map.of(
                "user", identity.getPrincipal().getName(),
                "roles", identity.getRoles()
        );
    }
}

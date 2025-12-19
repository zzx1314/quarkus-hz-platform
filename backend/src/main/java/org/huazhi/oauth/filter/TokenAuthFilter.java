package org.huazhi.oauth.filter;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.huazhi.oauth.annotation.TokenRequired;
import org.huazhi.util.R;
import org.huazhi.util.RedisUtil;
import io.smallrye.jwt.auth.principal.JWTParser;

@Provider
@TokenRequired
@Priority(Priorities.AUTHENTICATION)
public class TokenAuthFilter implements ContainerRequestFilter {

    @Inject
    RedisUtil redisUtil;

    @Inject
    JWTParser jwtParser;

    @Override
    public void filter(ContainerRequestContext requestContext) {

        String authHeader = requestContext.getHeaderString("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            abort(requestContext, "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);
        String key = "access_token:" + token;

        if (!redisUtil.exists(key)) {
            abort(requestContext, "Invalid access token");
            return;
        }

        try {
            JsonWebToken jwt = jwtParser.parse(token);
            if (!"https://quarkus-oauth2-server".equals(jwt.getIssuer())) {
                abort(requestContext, "Invalid issuer");
                return;
            }
        } catch (Exception e) {
            abort(requestContext, "Invalid token");
        }
    }

    private void abort(ContainerRequestContext ctx, String msg) {
        ctx.abortWith(
            Response.status(401)
                .entity(R.failed("UNAUTHORIZED", msg))
                .build()
        );
    }
}


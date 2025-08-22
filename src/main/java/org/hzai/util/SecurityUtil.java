package org.hzai.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.jwt.JsonWebToken;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SecurityUtil {

    @Inject
    JsonWebToken jwt;

    public List<Long> getRole() {
        Object roleObj = jwt.getClaim("role");
        if (roleObj instanceof List<?> list) {
            return list.stream()
                    .map(o -> {
                        if (o instanceof Number n)
                            return n.longValue();
                        return Long.valueOf(o.toString());
                    })
                    .toList();
        }
        return new ArrayList<>();
    }

    public String getUserName() {
        return jwt.getSubject();
    }

    public Long getUserId() {
        return Long.valueOf(jwt.getClaim("userId").toString());
    }

}
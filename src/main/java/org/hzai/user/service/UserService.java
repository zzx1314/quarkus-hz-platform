package org.hzai.user.service;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserService {
    public boolean authenticate(String username, String password) {
        return "admin".equals(username) && "123456".equals(password);
    }
}

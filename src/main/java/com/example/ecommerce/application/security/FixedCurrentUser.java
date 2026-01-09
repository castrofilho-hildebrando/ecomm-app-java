package com.example.ecommerce.application.security;

import com.example.ecommerce.domain.user.UserId;
import com.example.ecommerce.domain.user.UserRole;

public class FixedCurrentUser implements CurrentUser {

    private final UserId userId;
    private final UserRole role;

    public FixedCurrentUser(String userId) {
        this.userId = new UserId(userId);
        this.role = UserRole.CLIENT;
    }

    public FixedCurrentUser(String userId, UserRole role) {
        this.userId = new UserId(userId);
        this.role = role;
    }

    @Override
    public UserId id() {
        return userId;
    }

    @Override
    public UserRole role() {
        return role;
    }

    @Override
    public boolean hasRole(UserRole role) {
        return this.role == role;
    }
}

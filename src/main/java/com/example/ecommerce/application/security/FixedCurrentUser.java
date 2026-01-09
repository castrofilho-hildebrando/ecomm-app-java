package com.example.ecommerce.application.security;

import com.example.ecommerce.domain.user.UserId;
import com.example.ecommerce.domain.user.UserRole;

public class FixedCurrentUser implements CurrentUser {
    private final UserId userId;

    public FixedCurrentUser(String userId) {
        this.userId = new UserId(userId);
    }

    @Override
    public UserId id() { return userId; }

    @Override
    public UserRole role() { return UserRole.CLIENT; }

    @Override
    public boolean hasRole(UserRole role) { 
        return role == UserRole.CLIENT; 
    }
}
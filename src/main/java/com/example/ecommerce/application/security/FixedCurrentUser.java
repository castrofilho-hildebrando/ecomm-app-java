package com.example.ecommerce.application.security;

import com.example.ecommerce.domain.user.UserId;

public class FixedCurrentUser implements CurrentUser {

    private final UserId userId;

    public FixedCurrentUser(String userId) {
        this.userId = new UserId(userId);
    }

    @Override
    public UserId id() {
        return userId;
    }
}

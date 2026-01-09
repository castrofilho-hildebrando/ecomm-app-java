package com.example.ecommerce.application.security;

import com.example.ecommerce.domain.user.UserRole;

public final class AdminPolicy {

    private AdminPolicy() {
    }

    public static void assertAdmin(CurrentUser currentUser) {
        if (!currentUser.hasRole(UserRole.ADMIN)) {
            throw new AccessDeniedException();
        }
    }
}

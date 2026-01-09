package com.example.ecommerce.application.security;

import com.example.ecommerce.domain.user.UserRole;
import com.example.ecommerce.domain.exception.DomainException;

public final class AdminPolicy {

    private AdminPolicy() {
    }

    public static void assertAdmin(CurrentUser currentUser) {
        if (!currentUser.hasRole(UserRole.ADMIN)) {
            throw new DomainException("Admin privileges required") {};
        }
    }
}

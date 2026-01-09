package com.example.ecommerce.application.security;

import com.example.ecommerce.domain.user.UserId;
import com.example.ecommerce.domain.user.UserRole;

public interface CurrentUser {
    UserId id();
    UserRole role();
    boolean hasRole(UserRole role);
}

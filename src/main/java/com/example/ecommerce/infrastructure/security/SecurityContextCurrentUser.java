package com.example.ecommerce.infrastructure.security;

import com.example.ecommerce.application.security.CurrentUser;
import com.example.ecommerce.domain.user.UserId;
import com.example.ecommerce.domain.user.UserRole;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class SecurityContextCurrentUser implements CurrentUser {

    private final Authentication authentication;

    public SecurityContextCurrentUser(Authentication authentication) {
        this.authentication = authentication;
    }

    @Override
    public UserId id() {
        return new UserId(authentication.getName());
    }

    @Override
    public UserRole role() {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(UserRole::from)
                .findFirst()
                .orElse(UserRole.CLIENT);
    }

    @Override
    public boolean hasRole(UserRole role) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(UserRole::from)
                .anyMatch(r -> r == role);
    }
}

package com.example.ecommerce.domain.user;

public enum UserRole {
    CLIENT, ADMIN;

    // Added to fix "cannot find symbol: method from()" error
    public static UserRole from(String value) {
        try {
            return UserRole.valueOf(value.toUpperCase().replace("ROLE_", ""));
        } catch (Exception e) {
            return CLIENT;
        }
    }
}
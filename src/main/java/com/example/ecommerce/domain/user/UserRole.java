package com.example.ecommerce.domain.user;

public enum UserRole {
    CLIENT,
    ADMIN;

    public static UserRole from(String authority) {
        if (authority == null) {
            return CLIENT;
        }

        return switch (authority) {
            case "ADMIN", "ROLE_ADMIN" -> ADMIN;
            case "CLIENT", "ROLE_CLIENT", "USER", "ROLE_USER" -> CLIENT;
            default -> CLIENT;
        };
    }
}


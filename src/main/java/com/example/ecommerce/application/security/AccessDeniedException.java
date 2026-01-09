package com.example.ecommerce.application.security;

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException() {
        super("Access denied");
    }
}

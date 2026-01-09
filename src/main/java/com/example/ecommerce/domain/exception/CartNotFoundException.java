package com.example.ecommerce.domain.exception;

public class CartNotFoundException extends DomainException {

    public CartNotFoundException(String cartId) {
        super("Cart not found: " + cartId);
    }
}

package com.example.ecommerce.domain.exception;

public class CartItemNotFoundException extends DomainException {

    public CartItemNotFoundException(String productId) {
        super("Item not found in cart: " + productId);
    }
}

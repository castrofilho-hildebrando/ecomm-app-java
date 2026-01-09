package com.example.ecommerce.domain.exception;

public class EmptyCartException extends DomainException {

    public EmptyCartException(String cartId) {
        super("Cart is empty: " + cartId);
    }
}

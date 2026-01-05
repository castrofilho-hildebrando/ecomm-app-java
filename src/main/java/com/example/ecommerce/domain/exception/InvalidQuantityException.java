package com.example.ecommerce.domain.exception;

public class InvalidQuantityException extends DomainException {

    public InvalidQuantityException(int quantity) {
        super("Invalid quantity: " + quantity);
    }
}

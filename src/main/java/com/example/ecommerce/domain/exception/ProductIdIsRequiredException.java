package com.example.ecommerce.domain.exception;

public class ProductIdIsRequiredException extends DomainException {

    public ProductIdIsRequiredException() {
        super("Product id is required.");
    }
}

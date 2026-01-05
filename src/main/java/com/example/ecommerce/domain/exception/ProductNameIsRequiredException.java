package com.example.ecommerce.domain.exception;

public class ProductNameIsRequiredException extends DomainException {

    public ProductNameIsRequiredException() {
        super("Product name is required.");
    }
}

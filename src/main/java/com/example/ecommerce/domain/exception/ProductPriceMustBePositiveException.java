package com.example.ecommerce.domain.exception;

public class ProductPriceMustBePositiveException extends DomainException {

    public ProductPriceMustBePositiveException() {
        super("Product price must be positive");
    }
}

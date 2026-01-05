package com.example.ecommerce.domain.exception;

public class OrderNotFoundException extends DomainException {

    public OrderNotFoundException(String orderId) {
        super("Order not found: " + orderId);
    }
}
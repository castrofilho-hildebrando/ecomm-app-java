package com.example.ecommerce.domain.exception;

import com.example.ecommerce.domain.order.OrderId;

public class OrderAlreadyPaidException extends DomainException {

    public OrderAlreadyPaidException(OrderId orderId) {
        super("Order already paid: " + orderId);
    }
}
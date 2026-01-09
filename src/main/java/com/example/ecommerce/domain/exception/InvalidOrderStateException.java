package com.example.ecommerce.domain.exception;

import com.example.ecommerce.domain.order.OrderId;
import com.example.ecommerce.domain.order.OrderStatus;

public class InvalidOrderStateException extends DomainException {

    public InvalidOrderStateException(OrderId orderId, OrderStatus status) {
        super("Order " + orderId.value() + " is in invalid state: " + status);
    }
}

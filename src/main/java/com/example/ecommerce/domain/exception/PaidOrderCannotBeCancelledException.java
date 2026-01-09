package com.example.ecommerce.domain.exception;

import com.example.ecommerce.domain.order.OrderId;

public class PaidOrderCannotBeCancelledException extends DomainException {

    public PaidOrderCannotBeCancelledException(OrderId orderId) {
        super("Paid order cannot be cancelled: " + orderId);
    }
}
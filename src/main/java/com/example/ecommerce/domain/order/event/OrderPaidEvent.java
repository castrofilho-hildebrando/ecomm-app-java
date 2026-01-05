package com.example.ecommerce.domain.order.event;

import com.example.ecommerce.domain.event.DomainEvent;
import com.example.ecommerce.domain.order.OrderId;

import java.time.Instant;

public record OrderPaidEvent(
        OrderId orderId,
        Instant occurredAt
) implements DomainEvent {

    public OrderPaidEvent(OrderId orderId) {
        this(orderId, Instant.now());
    }
}

package com.example.ecommerce.domain.order.event;

import com.example.ecommerce.domain.event.DomainEvent;
import com.example.ecommerce.domain.order.OrderId;

import java.time.Instant;

public record OrderCreatedEvent(
        OrderId orderId,
        Instant occurredAt
) implements DomainEvent {

    public OrderCreatedEvent(OrderId orderId) {
        this(orderId, Instant.now());
    }
}

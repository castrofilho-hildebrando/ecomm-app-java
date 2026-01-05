package com.example.ecommerce.domain.order;

import com.example.ecommerce.domain.event.DomainEvent;
import com.example.ecommerce.domain.order.event.OrderCreatedEvent;
import com.example.ecommerce.domain.order.event.OrderPaidEvent;
import com.example.ecommerce.domain.exception.OrderAlreadyPaidException;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class Order {

    private final OrderId id;
    private final List<OrderItem> items;
    private final List<DomainEvent> domainEvents = new ArrayList<>();
    private OrderStatus status;

    public Order(OrderId id, List<OrderItem> items) {
        this.id = Objects.requireNonNull(id);
        this.items = List.copyOf(items);
        this.status = OrderStatus.CREATED;

         domainEvents.add(new OrderCreatedEvent(id));
    }

    private Order(
        OrderId id,
        List<OrderItem> items,
        OrderStatus status
    ) {
        this.id = id;
        this.items = List.copyOf(items);
        this.status = status;
    }

    public OrderId getId() {
        return id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void markAsPaid() {
        if (status != OrderStatus.CREATED) {
            throw new OrderAlreadyPaidException(id);
        }
        this.status = OrderStatus.PAID;
        domainEvents.add(new OrderPaidEvent(id));
    }

    public void cancel() {
        if (status == OrderStatus.PAID) {
            throw new IllegalStateException(
                    "Paid order cannot be cancelled"
            );
        }
        this.status = OrderStatus.CANCELLED;
    }

    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = List.copyOf(domainEvents);
        domainEvents.clear();
        return events;
    }

    public static Order rehydrate(
        OrderId id,
        List<OrderItem> items,
        OrderStatus status
    ) {
        return new Order(id, items, status);
    }
}

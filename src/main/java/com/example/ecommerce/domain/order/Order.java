package com.example.ecommerce.domain.order;

import com.example.ecommerce.domain.event.DomainEvent;
import com.example.ecommerce.domain.order.event.OrderCreatedEvent;
import com.example.ecommerce.domain.order.event.OrderPaidEvent;
import com.example.ecommerce.domain.exception.OrderAlreadyPaidException;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private final OrderId id;
    private final List<OrderItem> items = new ArrayList<>();
    private OrderStatus status = OrderStatus.CREATED;

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public Order(OrderId id) {
        this.id = id;
        this.domainEvents.add(new OrderCreatedEvent(id));
    }

    private Order(OrderId id, List<OrderItem> items, OrderStatus status) {
        this.id = id;
        this.items.addAll(items);
        this.status = status;
    }

    public static Order rehydrate(
            OrderId id,
            List<OrderItem> items,
            OrderStatus status
    ) {
        return new Order(id, items, status);
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public void markAsPaid() {
        if (status == OrderStatus.PAID) {
            throw new OrderAlreadyPaidException(id);
        }
        this.status = OrderStatus.PAID;
        domainEvents.add(new OrderPaidEvent(id));
    }

    public OrderId getId() {
        return id;
    }

    public List<OrderItem> getItems() {
        return List.copyOf(items);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = List.copyOf(domainEvents);
        domainEvents.clear();
        return events;
    }
}

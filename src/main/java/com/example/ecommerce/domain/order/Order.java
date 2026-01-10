package com.example.ecommerce.domain.order;

import com.example.ecommerce.domain.event.DomainEvent;
import com.example.ecommerce.domain.order.event.OrderCreatedEvent;
import com.example.ecommerce.domain.order.event.OrderPaidEvent;
import com.example.ecommerce.domain.exception.OrderAlreadyPaidException;
import com.example.ecommerce.domain.exception.PaidOrderCannotBeCancelledException;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    private List<OrderItem> items;

    private OrderStatus status;

    @Version
    private Long version;

    @Transient
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    /* ---------- Constructors ---------- */

    protected Order() {
        // Required by Spring Data
    }

    public Order(OrderId id, List<OrderItem> items) {
        this.id = Objects.requireNonNull(id).value();
        this.items = List.copyOf(items);
        this.status = OrderStatus.CREATED;
        domainEvents.add(new OrderCreatedEvent(id));
    }

    private Order(String id, List<OrderItem> items, OrderStatus status) {
        this.id = id;
        this.items = List.copyOf(items);
        this.status = status;
    }

    /* ---------- Domain API ---------- */

    public OrderId getId() {
        return new OrderId(id);
    }

    public List<OrderItem> getItems() {
        return List.copyOf(items);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void markAsPaid() {
        if (status != OrderStatus.CREATED) {
            throw new OrderAlreadyPaidException(getId());
        }
        this.status = OrderStatus.PAID;
        domainEvents.add(new OrderPaidEvent(getId()));
    }

    public void cancel() {
        if (status == OrderStatus.PAID) {
            throw new PaidOrderCannotBeCancelledException(getId());
        }
        this.status = OrderStatus.CANCELLED;
    }

    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = List.copyOf(domainEvents);
        domainEvents.clear();
        return events;
    }

    /* ---------- Rehydration ---------- */

    public static Order rehydrate(
            OrderId id,
            List<OrderItem> items,
            OrderStatus status
    ) {
        return new Order(id.value(), items, status);
    }
}

package com.example.ecommerce.domain.order;

import com.example.ecommerce.domain.event.DomainEvent;

import java.util.List;

public OrderTest {

    @Test
    void shouldRegisterOrderCreatedEvent() {
        Order order = new Order(
                new OrderId("order-1"),
                List.of(new OrderItem(new ProductId("product-1"), 1))
        );

        var events = order.pullDomainEvents();

        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof OrderCreatedEvent);
    }

    @Test
    void shouldRegisterOrderPaidEvent() {
        Order order = new Order(
                new OrderId("order-1"),
                List.of(new OrderItem(new ProductId("product-1"), 1))
        );

        order.pullDomainEvents(); // limpa OrderCreated

        order.markAsPaid();

        var events = order.pullDomainEvents();

        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof OrderPaidEvent);
    }

    @Test
    void shouldThrowWhenPayingAlreadyPaidOrder() {
        Order order = new Order(
                new OrderId("order-1"),
                List.of(new OrderItem(new ProductId("product-1"), 1))
        );

        order.pullDomainEvents(); // clear created
        order.markAsPaid();

        assertThrows(
                OrderAlreadyPaidException.class,
                order::markAsPaid
        );
    }
}
package com.example.ecommerce.domain.order;

import com.example.ecommerce.domain.cart.ProductId;
import com.example.ecommerce.domain.event.DomainEvent;
import com.example.ecommerce.domain.exception.OrderAlreadyPaidException;
import com.example.ecommerce.domain.order.event.OrderCreatedEvent;
import com.example.ecommerce.domain.order.event.OrderPaidEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class OrderTest {

    @Test
    void shouldRegisterOrderCreatedEvent() {
        Order order = new Order(
                new OrderId("order-1"),
                List.of(new OrderItem(new OrderProductId("product-1"), 1))
        );

        var events = order.pullDomainEvents();

        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof OrderCreatedEvent);
    }

    @Test
    void shouldRegisterOrderPaidEvent() {
        Order order = new Order(
                new OrderId("order-1"),
                List.of(new OrderItem(new OrderProductId("product-1"), 1))
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
                List.of(new OrderItem(new OrderProductId("product-1"), 1))
        );

        order.pullDomainEvents(); // clear created
        order.markAsPaid();

        assertThrows(
                OrderAlreadyPaidException.class,
                order::markAsPaid
        );
    }
}
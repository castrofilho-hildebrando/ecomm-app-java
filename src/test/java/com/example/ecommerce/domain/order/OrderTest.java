package com.example.ecommerce.domain.order;

import com.example.ecommerce.domain.cart.ProductId;
import com.example.ecommerce.domain.order.event.OrderCreatedEvent;
import com.example.ecommerce.domain.order.event.OrderPaidEvent;
import com.example.ecommerce.domain.exception.OrderAlreadyPaidException;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void shouldRegisterOrderCreatedEvent() {
        Order order = new Order(new OrderId("order-1"));

        var events = order.pullDomainEvents();

        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof OrderCreatedEvent);
    }

    @Test
    void shouldRegisterOrderPaidEvent() {
        Order order = new Order(new OrderId("order-1"));
        order.addItem(new OrderItem(
                new ProductId("product-1"),
                BigDecimal.TEN,
                1
        ));

        order.pullDomainEvents(); // clear OrderCreated
        order.markAsPaid();

        var events = order.pullDomainEvents();

        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof OrderPaidEvent);
    }

    @Test
    void shouldThrowWhenPayingAlreadyPaidOrder() {
        Order order = new Order(new OrderId("order-1"));
        order.addItem(new OrderItem(
                new ProductId("product-1"),
                BigDecimal.TEN,
                1
        ));

        order.pullDomainEvents();
        order.markAsPaid();

        assertThrows(
                OrderAlreadyPaidException.class,
                order::markAsPaid
        );
    }
}

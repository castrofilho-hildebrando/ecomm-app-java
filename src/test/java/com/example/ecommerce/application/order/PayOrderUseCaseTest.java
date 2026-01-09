package com.example.ecommerce.application.order;

import com.example.ecommerce.domain.order.*;
import com.example.ecommerce.domain.cart.ProductId;
import com.example.ecommerce.domain.order.event.OrderPaidEvent;
import com.example.ecommerce.domain.exception.OrderNotFoundException;
import com.example.ecommerce.domain.exception.OrderAlreadyPaidException;
import com.example.ecommerce.infrastructure.persistence.memory.order.InMemoryOrderRepository;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PayOrderUseCaseTest {

    @Test
    void shouldPayOrderAndPublishEvent() {
        InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();
        FakeDomainEventPublisher publisher = new FakeDomainEventPublisher();

        Order order = new Order(new OrderId("order-1"));
        order.addItem(new OrderItem(
                new ProductId("product-1"),
                BigDecimal.TEN,
                2
        ));

        order.pullDomainEvents();
        orderRepository.save(order);

        PayOrderUseCase useCase =
                new PayOrderUseCase(orderRepository, publisher);

        useCase.execute("order-1");

        Order persisted = orderRepository
                .findById(new OrderId("order-1"))
                .orElseThrow();

        assertEquals(OrderStatus.PAID, persisted.getStatus());
        assertEquals(1, publisher.count());
        assertTrue(publisher.first() instanceof OrderPaidEvent);
    }

    @Test
    void shouldFailWhenOrderDoesNotExist() {
        PayOrderUseCase useCase =
                new PayOrderUseCase(
                        new InMemoryOrderRepository(),
                        event -> {}
                );

        assertThrows(
                OrderNotFoundException.class,
                () -> useCase.execute("missing-order")
        );
    }

    @Test
    void shouldFailWhenOrderIsAlreadyPaid() {
        InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();

        Order order = new Order(new OrderId("order-1"));
        order.addItem(new OrderItem(
                new ProductId("product-1"),
                BigDecimal.TEN,
                2
        ));
        order.markAsPaid();

        orderRepository.save(order);

        PayOrderUseCase useCase =
                new PayOrderUseCase(orderRepository, event -> {});

        assertThrows(
                OrderAlreadyPaidException.class,
                () -> useCase.execute("order-1")
        );
    }
}

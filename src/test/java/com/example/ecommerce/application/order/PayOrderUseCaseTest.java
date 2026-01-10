package com.example.ecommerce.application.order;

import com.example.ecommerce.application.security.FixedCurrentUser;
import com.example.ecommerce.domain.order.Order;
import com.example.ecommerce.domain.order.OrderId;
import com.example.ecommerce.domain.order.OrderItem;
import com.example.ecommerce.domain.order.OrderStatus;
import com.example.ecommerce.domain.order.OrderProductId;
import com.example.ecommerce.domain.order.event.OrderPaidEvent;
import com.example.ecommerce.domain.exception.OrderNotFoundException;
import com.example.ecommerce.domain.exception.OrderAlreadyPaidException;
import com.example.ecommerce.infrastructure.persistence.memory.order.InMemoryOrderRepository;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PayOrderUseCaseTest {

    @Test
    void shouldPayOrderAndPublishEvent() {
        InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();
        FakeDomainEventPublisher publisher = new FakeDomainEventPublisher();

        orderRepository.clear();

        Order order = new Order(
                new OrderId("order-1"),
                List.of(new OrderItem(new OrderProductId("product-1"), 1))
        );

        order.pullDomainEvents();

        orderRepository.save(order);

        PayOrderUseCase useCase = new PayOrderUseCase(
                orderRepository,
                new InMemoryIdempotencyRepository(),
                publisher
        );

        useCase.execute(
                "order-1",
                new FixedCurrentUser("user-1"),
                "idem-key-1"
        );

        Order persisted = orderRepository
                .findById(new OrderId("order-1"))
                .orElseThrow();

        assertEquals(OrderStatus.PAID, persisted.getStatus());

        assertEquals(1, publisher.count());
        assertTrue(publisher.first() instanceof OrderPaidEvent);
    }

    @Test
    void shouldFailWhenOrderDoesNotExist() {
        PayOrderUseCase useCase = new PayOrderUseCase(
                new InMemoryOrderRepository(),
                new InMemoryIdempotencyRepository(),
                event -> {}
        );

        assertThrows(
                OrderNotFoundException.class,
                () -> useCase.execute(
                        "missing-order",
                        new FixedCurrentUser("user-1"),
                        "idem-key-1"
                )
        );
    }

    @Test
    void shouldFailWhenOrderIsAlreadyPaid() {
        InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();

        orderRepository.clear();

        Order order = new Order(
                new OrderId("order-1"),
                List.of(new OrderItem(new OrderProductId("product-1"), 1))
        );
        order.markAsPaid();

        orderRepository.save(order);

        PayOrderUseCase useCase = new PayOrderUseCase(
                orderRepository,
                new InMemoryIdempotencyRepository(),
                event -> {}
        );

        assertThrows(
                OrderAlreadyPaidException.class,
                () -> useCase.execute(
                        "order-1",
                        new FixedCurrentUser("user-1"),
                        "idem-key-2"
                )
        );
    }
}
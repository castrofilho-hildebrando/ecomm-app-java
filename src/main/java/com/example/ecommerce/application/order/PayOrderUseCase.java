package com.example.ecommerce.application.order;

import com.example.ecommerce.application.event.DomainEventPublisher;
import com.example.ecommerce.domain.order.Order;
import com.example.ecommerce.domain.order.OrderId;
import com.example.ecommerce.domain.order.OrderRepository;
import com.example.ecommerce.domain.exception.OrderNotFoundException;
import com.example.ecommerce.application.event.EventConsumer;
import com.example.ecommerce.domain.event.DomainEvent;

public class PayOrderUseCase {

    private final OrderRepository orderRepository;
    private final DomainEventPublisher eventPublisher;

    public PayOrderUseCase(
            OrderRepository orderRepository,
            DomainEventPublisher eventPublisher
    ) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    public void execute(String orderId) {
        OrderId id = new OrderId(orderId);

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(orderId));

        order.markAsPaid();
        orderRepository.save(order);

        EventConsumer consumer = new EventConsumer(eventPublisher);

        for (DomainEvent event : order.pullDomainEvents()) {
            consumer.accept(event);
        }

        for (DomainEvent event : aggregate.pullDomainEvents()) {
            publisher.publish(event);
        }
    }
}

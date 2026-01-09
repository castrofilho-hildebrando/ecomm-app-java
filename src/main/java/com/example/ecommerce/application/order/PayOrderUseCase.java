package com.example.ecommerce.application.order;

import com.example.ecommerce.application.event.DomainEventPublisher;
import com.example.ecommerce.domain.order.Order;
import com.example.ecommerce.domain.order.OrderId;
import com.example.ecommerce.domain.order.OrderRepository;
import com.example.ecommerce.domain.event.DomainEvent;
import com.example.ecommerce.domain.exception.OrderNotFoundException;

public class PayOrderUseCase {
    private final OrderRepository orderRepository;
    private final DomainEventPublisher publisher; // Define publisher

    public PayOrderUseCase(OrderRepository orderRepository, DomainEventPublisher publisher) {
        this.orderRepository = orderRepository;
        this.publisher = publisher;
    }

    public void execute(String orderId) {
        Order order = orderRepository.findById(new OrderId(orderId)) // use 'order' instead of 'aggregate'
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.markAsPaid();
        
        // Use the defined 'order' variable and 'publisher' field
        for (DomainEvent event : order.pullDomainEvents()) {
            publisher.publish(event);
        }
        
        orderRepository.save(order);
    }
}
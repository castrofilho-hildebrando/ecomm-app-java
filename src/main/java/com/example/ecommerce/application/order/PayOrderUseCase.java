// PayOrderUseCase.java
package com.example.ecommerce.application.order;

import com.example.ecommerce.domain.order.Order;
import com.example.ecommerce.domain.order.OrderId;
import com.example.ecommerce.domain.order.OrderRepository;
import com.example.ecommerce.infrastructure.web.order.OrderView;
import com.example.ecommerce.infrastructure.web.order.OrderItemView;
import com.example.ecommerce.domain.exception.OrderNotFoundException;
import com.example.ecommerce.application.event.DomainEventPublisher;

import java.util.List;

public class PayOrderUseCase {

    private final OrderRepository orderRepository;
    private final DomainEventPublisher publisher;

    public PayOrderUseCase(OrderRepository orderRepository, DomainEventPublisher publisher) {
        this.orderRepository = orderRepository;
        this.publisher = publisher;
    }

    public OrderView execute(String orderId) {
        Order order = orderRepository.findById(new OrderId(orderId))
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.markAsPaid();
        orderRepository.save(order);

        order.pullDomainEvents().forEach(publisher::publish);

        List<OrderItemView> itemViews = order.getItems().stream()
                .map(i -> new OrderItemView(
                        i.getProductId().value(),
                        i.getQuantity()
                ))
                .toList();

        return new OrderView(
                order.getId().value(),
                order.getStatus().name(),
                itemViews
        );
    }
}

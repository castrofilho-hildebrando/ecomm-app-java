package com.example.ecommerce.application.order;

import com.example.ecommerce.domain.exception.OrderNotFoundException;
import com.example.ecommerce.domain.order.Order;
import com.example.ecommerce.domain.order.OrderId;
import com.example.ecommerce.domain.order.OrderRepository;
import com.example.ecommerce.infrastructure.web.order.OrderView;
import com.example.ecommerce.infrastructure.web.order.OrderItemView;

public class GetOrderUseCase {

    private final OrderRepository orderRepository;

    public GetOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

public OrderView execute(String orderId) {
    Order order = orderRepository
            .findById(new OrderId(orderId))
            .orElseThrow(() -> new OrderNotFoundException(orderId));

    return new OrderView(
            order.getId().value(),
            order.getStatus().name(),
            order.getItems().stream()
                    .map(item -> new OrderItemView(
                            item.getProductId().value(),
                            item.getQuantity()
                    ))
                    .toList()
    );
}

}

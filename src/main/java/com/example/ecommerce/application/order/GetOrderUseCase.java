package com.example.ecommerce.application.order;

import com.example.ecommerce.application.security.CurrentUser;
import com.example.ecommerce.domain.order.Order;
import com.example.ecommerce.domain.order.OrderId;
import com.example.ecommerce.domain.order.OrderRepository;
import com.example.ecommerce.domain.exception.OrderNotFoundException;

public class GetOrderUseCase {

    private final OrderRepository orderRepository;

    public GetOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderView execute(String orderId, CurrentUser currentUser) {
        Order order = orderRepository.findById(new OrderId(orderId))
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        return OrderView.from(order);
    }
}

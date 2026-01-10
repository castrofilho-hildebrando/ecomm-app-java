package com.example.ecommerce.application.order;

import com.example.ecommerce.domain.order.Order;

import java.util.List;

public record OrderView(
        String id,
        List<OrderItemView> items,
        String status
) {
    public static OrderView from(Order order) {
        return new OrderView(
                order.getId().value(),
                order.getItems().stream()
                        .map(i -> new OrderItemView(
                                i.getProductId().value(),
                                i.getQuantity()
                        ))
                        .toList(),
                order.getStatus().name()
        );
    }
}

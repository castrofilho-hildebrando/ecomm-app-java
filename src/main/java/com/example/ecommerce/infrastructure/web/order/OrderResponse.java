package com.example.ecommerce.infrastructure.web.order;

import com.example.ecommerce.application.order.OrderItemView;
import com.example.ecommerce.application.order.OrderView;

import java.util.List;

public record OrderResponse(
        String id,
        String status,
        List<OrderItemResponse> items
) {

    public static OrderResponse from(OrderView view) {
        return new OrderResponse(
                view.id(),
                view.status(),
                view.items().stream()
                        .map(OrderItemResponse::from)
                        .toList()
        );
    }
}

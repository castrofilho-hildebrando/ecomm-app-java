package com.example.ecommerce.infrastructure.web.order;

import com.example.ecommerce.application.order.OrderItemView;

public record OrderItemResponse(
        String productId,
        int quantity
) {

    public static OrderItemResponse from(OrderItemView view) {
        return new OrderItemResponse(
                view.productId(),
                view.quantity()
        );
    }
}

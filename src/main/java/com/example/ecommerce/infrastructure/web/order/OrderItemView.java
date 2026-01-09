package com.example.ecommerce.infrastructure.web.order;

public record OrderItemView(
        String productId,
        int quantity
) {
}

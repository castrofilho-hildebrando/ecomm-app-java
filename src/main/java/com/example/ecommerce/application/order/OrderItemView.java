package com.example.ecommerce.application.order;

public record OrderItemView(
        String productId,
        int quantity
) {
}

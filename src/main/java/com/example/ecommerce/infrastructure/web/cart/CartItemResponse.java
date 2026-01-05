package com.example.ecommerce.infrastructure.web.cart;

public record CartItemResponse(
        String productId,
        int quantity
) {
}

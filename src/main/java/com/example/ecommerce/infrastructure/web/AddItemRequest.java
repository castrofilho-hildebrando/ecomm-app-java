package com.example.ecommerce.infrastructure.web.cart;

public record AddItemRequest(
        String productId,
        int quantity
) {
}

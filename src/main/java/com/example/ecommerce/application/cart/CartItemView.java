package com.example.ecommerce.application.cart;

import com.example.ecommerce.domain.cart.CartItem;

public record CartItemView(
        String productId,
        int quantity
) {
    public static CartItemView from(CartItem item) {
        return new CartItemView(
                item.getProductId().value(),
                item.getQuantity()
        );
    }
}

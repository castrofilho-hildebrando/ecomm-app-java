package com.example.ecommerce.infrastructure.web.cart;

import com.example.ecommerce.application.cart.CartItemView;

public record CartItemResponse(
        String productId,
        int quantity
) {

    public static CartItemResponse from(CartItemView view) {
        return new CartItemResponse(
                view.productId(),
                view.quantity()
        );
    }
}

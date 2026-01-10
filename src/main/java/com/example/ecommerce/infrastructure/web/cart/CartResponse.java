package com.example.ecommerce.infrastructure.web.cart;

import com.example.ecommerce.application.cart.CartView;

import java.util.List;

public record CartResponse(
        String id,
        List<CartItemResponse> items
) {

    public static CartResponse from(CartView view) {
        return new CartResponse(
                view.id(),
                view.items().stream()
                        .map(CartItemResponse::from)
                        .toList()
        );
    }
}

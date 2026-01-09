package com.example.ecommerce.infrastructure.web.cart;

import com.example.ecommerce.application.cart.CartView;

import java.util.List;

public record CartResponse(
        String cartId,
        List<CartItemResponse> items
) {

    public static CartResponse from(CartView view) {
        return new CartResponse(
                view.cartId(),
                view.items().stream()
                        .map(item -> new CartItemResponse(
                                item.productId(),
                                item.quantity()
                        ))
                        .toList()
        );
    }
}

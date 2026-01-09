package com.example.ecommerce.application.cart;

import com.example.ecommerce.domain.cart.Cart;

import java.util.List;

public record CartView(
        String cartId,
        List<CartItemView> items
) {

    public static CartView from(Cart cart) {
        return new CartView(
                cart.getId().value(),
                cart.getItems().stream()
                        .map(item -> new CartItemView(
                                item.getProductId().value(),
                                item.getQuantity()
                        ))
                        .toList()
        );
    }
}

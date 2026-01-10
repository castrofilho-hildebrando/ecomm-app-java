package com.example.ecommerce.application.cart;

import com.example.ecommerce.domain.cart.Cart;
import com.example.ecommerce.domain.cart.CartItem;

import java.util.List;

public record CartView(
        String id,
        List<CartItemView> items
) {

    public static CartView from(Cart cart) {
        return new CartView(
                cart.getId().value(),
                cart.getItems().values().stream()
                        .map(CartItemView::from)
                        .toList()
        );
    }
}

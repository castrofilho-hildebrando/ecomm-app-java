package com.example.ecommerce.application.cart;

import java.util.List;

public record CartView(
        String cartId,
        List<CartItemView> items
) {
}

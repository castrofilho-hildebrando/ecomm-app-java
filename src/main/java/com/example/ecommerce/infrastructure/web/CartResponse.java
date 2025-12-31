package com.example.ecommerce.infrastructure.web.cart;

import java.util.List;

public record CartResponse(
        String cartId,
        List<CartItemResponse> items
) {
}

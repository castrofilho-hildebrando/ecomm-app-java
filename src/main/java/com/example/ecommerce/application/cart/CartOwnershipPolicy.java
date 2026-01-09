package com.example.ecommerce.application.cart;

import com.example.ecommerce.domain.cart.Cart;
import com.example.ecommerce.domain.user.UserId;
import com.example.ecommerce.domain.exception.CartNotFoundException;

public final class CartOwnershipPolicy {

    private CartOwnershipPolicy() {
    }

    public static void assertOwner(
            Cart cart,
            UserId userId
    ) {
        if (!cart.getOwnerId().equals(userId)) {
            throw new CartNotFoundException(cart.getId().value());
        }
    }
}

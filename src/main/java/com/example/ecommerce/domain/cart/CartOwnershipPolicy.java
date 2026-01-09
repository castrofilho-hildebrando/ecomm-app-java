package com.example.ecommerce.domain.cart;

import com.example.ecommerce.application.security.AccessDeniedException;
import com.example.ecommerce.domain.user.UserId;

public final class CartOwnershipPolicy {
    private CartOwnershipPolicy() {}

    public static void assertOwner(Cart cart, UserId userId) {
        if (!cart.getOwnerId().equals(userId)) {
            throw new AccessDeniedException();
        }
    }
}
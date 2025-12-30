package com.example.ecommerce.application.cart;

import com.example.ecommerce.domain.cart.Cart;
import com.example.ecommerce.domain.cart.CartRepository;

import java.util.UUID;

public class RemoveItemFromCartUseCase {

    private final CartRepository cartRepository;

    public RemoveItemFromCartUseCase(
            CartRepository cartRepository
    ) {
        this.cartRepository = cartRepository;
    }

    public void execute(UUID userId, UUID productId) {

        Cart cart = cartRepository.findActiveByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        cart.removeItem(productId);
        cartRepository.save(cart);
    }
}

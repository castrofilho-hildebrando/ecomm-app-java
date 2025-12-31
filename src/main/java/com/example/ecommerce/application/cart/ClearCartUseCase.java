package com.example.ecommerce.application.cart;

import com.example.ecommerce.domain.cart.Cart;
import com.example.ecommerce.domain.cart.CartRepository;

import java.util.UUID;

public class ClearCartUseCase {

    private final CartRepository cartRepository;

    public ClearCartUseCase(
            CartRepository cartRepository
    ) {
        this.cartRepository = cartRepository;
    }

    public void execute(UUID userId) {

        Cart cart = cartRepository.findActiveByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
    
        if (cart.isEmpty()) {
            throw new IllegalStateException("Cart is already empty");
        }
        
        cart.emptyCart();
        cartRepository.save(cart);
    }
}

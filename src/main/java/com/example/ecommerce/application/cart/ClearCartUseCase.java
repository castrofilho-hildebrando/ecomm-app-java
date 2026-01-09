package com.example.ecommerce.application.cart;

import com.example.ecommerce.application.security.CurrentUser; // Added
import com.example.ecommerce.domain.cart.*;
import com.example.ecommerce.domain.exception.CartNotFoundException;

public class ClearCartUseCase {
    private final CartRepository cartRepository;

    public ClearCartUseCase(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void execute(String cartId, CurrentUser currentUser) { // Fixed parameters
        Cart cart = cartRepository.findById(new CartId(cartId))
                .orElseThrow(() -> new CartNotFoundException(cartId));
        
        CartOwnershipPolicy.assertOwner(cart, currentUser.id());
        cart.clear();
        cartRepository.save(cart);
    }
}
package com.example.ecommerce.application.cart;

import com.example.ecommerce.domain.cart.*;

public class ClearCartUseCase {

    private final CartRepository cartRepository;

    public ClearCartUseCase(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void execute(String cartId) {
        CartId cid = new CartId(cartId);

        Cart cart = cartRepository.findById(cid)
                .orElseThrow(() -> new IllegalStateException("Cart not found"));

        cart.clear();
        cartRepository.save(cart);
    }
}

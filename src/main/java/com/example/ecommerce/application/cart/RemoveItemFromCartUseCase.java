package com.example.ecommerce.application.cart;

import com.example.ecommerce.domain.cart.*;

public class RemoveItemFromCartUseCase {

    private final CartRepository cartRepository;

    public RemoveItemFromCartUseCase(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void execute(String cartId, String productId) {
        CartId cid = new CartId(cartId);
        ProductId pid = new ProductId(productId);

        Cart cart = cartRepository.findById(cid)
                .orElseThrow(() -> new IllegalStateException("Cart not found"));

        cart.removeItem(pid);
        cartRepository.save(cart);
    }
}

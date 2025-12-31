package com.example.ecommerce.application.cart;

import com.example.ecommerce.domain.cart.*;

public class UpdateCartItemQuantityUseCase {

    private final CartRepository cartRepository;

    public UpdateCartItemQuantityUseCase(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void execute(String cartId, String productId, int quantity) {
        CartId cid = new CartId(cartId);
        ProductId pid = new ProductId(productId);

        Cart cart = cartRepository.findById(cid)
                .orElseThrow(() -> new IllegalStateException("Cart not found"));

        cart.updateItemQuantity(pid, quantity);
        cartRepository.save(cart);
    }
}

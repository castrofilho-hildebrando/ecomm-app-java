package com.example.ecommerce.application.cart;

import com.example.ecommerce.domain.cart.*;

public class AddItemToCartUseCase {

    private final CartRepository cartRepository;

    public AddItemToCartUseCase(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void execute(String cartId, String productId, int quantity) {
        CartId cid = new CartId(cartId);
        ProductId pid = new ProductId(productId);

        Cart cart = cartRepository.findById(cid)
                .orElseGet(() -> new Cart(cid));

        cart.addItem(pid, quantity);
        cartRepository.save(cart);
    }
}

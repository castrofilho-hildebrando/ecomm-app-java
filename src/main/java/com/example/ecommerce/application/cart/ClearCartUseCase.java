package com.example.ecommerce.application.cart;

import com.example.ecommerce.domain.cart.Cart;
import com.example.ecommerce.domain.cart.CartId;
import com.example.ecommerce.domain.cart.CartRepository;
import com.example.ecommerce.domain.user.UserId;
import com.example.ecommerce.domain.exception.CartNotFoundException;
import com.example.ecommerce.application.security.CurrentUser;

public class ClearCartUseCase {

    private final CartRepository cartRepository;

    public ClearCartUseCase(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void execute(String cartId, CurrentUser currentUser) {
        UserId uid = currentUser.id();
        Cart cart = cartRepository.findById(new CartId(cartId))
                .orElseThrow(() -> new CartNotFoundException(cartId));

        CartOwnershipPolicy.assertOwner(cart, uid);

        cart.validateNotEmpty();

        cart.clear();
        cartRepository.save(cart);
    }
}

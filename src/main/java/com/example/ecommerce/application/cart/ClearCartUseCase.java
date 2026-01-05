package com.example.ecommerce.application.cart;

import com.example.ecommerce.domain.cart.Cart;
import com.example.ecommerce.domain.cart.CartId;
import com.example.ecommerce.domain.cart.CartRepository;
import com.example.ecommerce.domain.exception.CartNotFoundException;

import com.example.ecommerce.domain.user.UserId;
import com.example.ecommerce.application.security.CurrentUser;

public class ClearCartUseCase {

    private final CartRepository cartRepository;

    public ClearCartUseCase(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void execute(String cartId, String userId) {
        CartId cid = new CartId(cartId);
        UserId uid = currentUser.id();

        Cart cart = cartRepository.findById(cid)
                .orElseThrow(() -> new CartNotFoundException(cartId));

        if (cart.isEmpty()) {
            throw new EmptyCartException(cartId);
        }

        CartOwnershipPolicy.assertOwner(cart, uid);

        cart.clear();
        cartRepository.save(cart);
    }
}

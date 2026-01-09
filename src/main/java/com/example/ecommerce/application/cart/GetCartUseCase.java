package com.example.ecommerce.application.cart;

import com.example.ecommerce.domain.cart.Cart;
import com.example.ecommerce.domain.cart.CartId;
import com.example.ecommerce.domain.cart.CartOwnershipPolicy;
import com.example.ecommerce.domain.cart.CartRepository;
import com.example.ecommerce.domain.exception.CartNotFoundException;
import com.example.ecommerce.application.security.CurrentUser;

public class GetCartUseCase {
    private final CartRepository cartRepository;

    public GetCartUseCase(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public CartView execute(String cartId, CurrentUser currentUser) {
        Cart cart = cartRepository.findById(new CartId(cartId))
                .orElseThrow(() -> new CartNotFoundException(cartId));
        
        // Validação de segurança obrigatória [cite: 139]
        CartOwnershipPolicy.assertOwner(cart, currentUser.id());
        
        return CartView.from(cart);
    }
}

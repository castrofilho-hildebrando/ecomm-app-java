package com.example.ecommerce.application.cart;

import com.example.ecommerce.domain.cart.Cart;
import com.example.ecommerce.domain.cart.CartId;
import com.example.ecommerce.domain.cart.CartOwnershipPolicy;
import com.example.ecommerce.domain.cart.ProductId;
import com.example.ecommerce.domain.cart.CartRepository;
import com.example.ecommerce.domain.exception.CartNotFoundException;
import com.example.ecommerce.domain.exception.EmptyCartException;
import com.example.ecommerce.domain.user.UserId;
import com.example.ecommerce.application.security.CurrentUser;

public class UpdateCartItemQuantityUseCase {

    private final CartRepository cartRepository;

    public UpdateCartItemQuantityUseCase(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public CartView execute(
        String cartId,
        CurrentUser currentUser,
        String productId,
        int quantity
    ) {
        UserId uid = currentUser.id();

        Cart cart = cartRepository.findById(new CartId(cartId))
                .orElseThrow(() -> new CartNotFoundException(cartId));

        if (cart.isEmpty()) {
            throw new EmptyCartException(cartId);
        }

        CartOwnershipPolicy.assertOwner(cart, uid);

        ProductId pid = new ProductId(productId);

        cart.updateItemQuantity(pid, quantity);
        cartRepository.save(cart);

        return CartView.from(cart);
    }
}

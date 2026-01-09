package com.example.ecommerce.application.cart;

import com.example.ecommerce.domain.cart.Cart;
import com.example.ecommerce.domain.cart.CartId;
import com.example.ecommerce.domain.cart.CartRepository;
import com.example.ecommerce.domain.user.UserId;
import com.example.ecommerce.domain.exception.CartNotFoundException;
import com.example.ecommerce.application.security.CurrentUser;

import java.util.List;

public class GetCartUseCase {

    private final CartRepository cartRepository;

    public GetCartUseCase(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public CartView execute(String cartId, CurrentUser currentUser) {

        CartId cid = new CartId(cartId);
        UserId uid = currentUser.id();

        Cart cart = cartRepository.findById(cid)
                .orElseThrow(() -> new CartNotFoundException(cartId));

        CartOwnershipPolicy.assertOwner(cart, uid);

        List<CartItemView> items = cart.getItems().stream()
                .map(item -> new CartItemView(
                        item.getProductId().value(),
                        item.getQuantity()
                ))
                .toList();

        return new CartView(cart.getId().value(), items);
    }
}

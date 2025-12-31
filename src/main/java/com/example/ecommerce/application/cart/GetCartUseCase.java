package com.example.ecommerce.application.cart;

import com.example.ecommerce.domain.cart.*;

import java.util.List;

public class GetCartUseCase {

    private final CartRepository cartRepository;

    public GetCartUseCase(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public CartView execute(String cartId) {

        CartId cid = new CartId(cartId);

        Cart cart = cartRepository.findById(cid)
                .orElse(new Cart(cid));

        List<CartItemView> items = cart.getItems().stream()
                .map(item -> new CartItemView(
                        item.getProductId().value(),
                        item.getQuantity()
                ))
                .toList();

        return new CartView(cart.getId().value(), items);
    }
}

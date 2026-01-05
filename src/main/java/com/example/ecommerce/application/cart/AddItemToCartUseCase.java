package com.example.ecommerce.application.cart;

import com.example.ecommerce.domain.cart.CartId;
import com.example.ecommerce.domain.cart.CartRepository;
import com.example.ecommerce.domain.user.UserId;
import com.example.ecommerce.application.security.CurrentUser;

public class AddItemToCartUseCase {

    private final CartRepository cartRepository;

    public AddItemToCartUseCase(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public CartView execute(
            String cartId,
            CurrentUser currentUser,
            String productId,
            int quantity
    ) {
        CartId cid = new CartId(cartId);
        UserId uid = currentUser.id();
        ProductId pid = new ProductId(productId);

        Cart cart = cartRepository.findById(cid)
                .orElseGet(() -> new Cart(cid, uid));

        CartOwnershipPolicy.assertOwner(cart, uid);

        cart.addItem(pid, quantity);
        cartRepository.save(cart);

        return CartView.from(cart);
    }
}

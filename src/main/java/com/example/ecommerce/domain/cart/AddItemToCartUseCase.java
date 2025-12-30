package com.example.ecommerce.application.cart;

import com.example.ecommerce.domain.cart.CartRepository;
import com.example.ecommerce.domain.product.ProductRepository;

import java.util.UUID;

public class AddItemToCartUseCase {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public AddItemToCartUseCase(
            ProductRepository productRepository,
            CartRepository cartRepository
    ) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    public void execute(UUID userId, UUID productId, int quantity) {

        var product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (!product.isActive()) {
            throw new IllegalStateException("Product is not active");
        }

        Cart cart = cartRepository.findActiveByUserId(userId)
                .orElseGet(() -> new Cart(userId));

        cart.addItem(
                product.id(),
                product.price(),
                quantity
        );

        cartRepository.save(cart);
    }
}

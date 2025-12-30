package com.example.ecommerce.application.cart;

import com.example.ecommerce.domain.product.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Test
    void shouldAddNewItemToEmptyCart() {
        var productRepository = new InMemoryProductRepository();
        var cartRepository = new InMemoryCartRepository();

        var productId = UUID.randomUUID();
        var userId = UUID.randomUUID();

        var product = new Product(
                productId,
                "Mouse",
                new BigDecimal("100.00"),
                true
        );

        productRepository.save(product);

        var useCase = new AddItemToCartUseCase(
                productRepository,
                cartRepository
        );

        useCase.execute(userId, productId, 2);

        var cart = cartRepository.findActiveByUserId(userId).orElseThrow();

        assertEquals(1, cart.items().size());
        assertEquals(new BigDecimal("200.00"), cart.total());
    }
package com.example.ecommerce.application.cart;

import com.example.ecommerce.domain.cart.Cart;
import com.example.ecommerce.domain.product.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class shouldFailWhenProductDoesNotExist {

    @Test
    void shouldFailWhenProductDoesNotExist() {
        var productRepository = new InMemoryProductRepository();
        var cartRepository = new InMemoryCartRepository();

        var useCase = new AddItemToCartUseCase(
                productRepository,
                cartRepository
        );

        var userId = UUID.randomUUID();
        var productId = UUID.randomUUID();

        assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(userId, productId, 1)
        );
    }
}
package com.example.ecommerce.application.cart;

import com.example.ecommerce.domain.product.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RemoveItemFromCartUseCaseTest {

    @Test
    void shouldRemoveExistingItemFromCart() {
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

        var addUseCase = new AddItemToCartUseCase(productRepository, cartRepository);
        addUseCase.execute(userId, productId, 2);

        var removeUseCase = new RemoveItemFromCartUseCase(cartRepository);
        removeUseCase.execute(userId, productId);

        var cart = cartRepository.findActiveByUserId(userId).orElseThrow();

        assertTrue(cart.isEmpty());
    }

    @Test
    void shouldFailWhenRemovingNonExistingProductInCart() {
        
        var cartRepository = new InMemoryCartRepository();
        var userId = UUID.randomUUID();

        {
            var productRepository = new InMemoryProductRepository();

            var productId = UUID.randomUUID();
            var product = new Product(
                    productId,
                    "Keyboard",
                    new BigDecimal("150.00"),
                    true
            );

            productRepository.save(product);

            var addUseCase = new AddItemToCartUseCase(productRepository, cartRepository);
            addUseCase.execute(userId, productId, 1);
        }

        var removeUseCase = new RemoveItemFromCartUseCase(cartRepository);
        var nonExistingProductId = UUID.randomUUID();

        assertThrows(
                IllegalStateException.class,
                () -> removeUseCase.execute(userId, nonExistingProductId)
        );
    }
}

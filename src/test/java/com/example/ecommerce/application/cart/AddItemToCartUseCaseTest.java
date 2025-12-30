package com.example.ecommerce.application.cart;

import com.example.ecommerce.domain.product.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AddItemToCartUseCaseTest {

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

    @Test
    void shouldFailWhenProductIsInactive() {
        var productRepository = new InMemoryProductRepository();
        var cartRepository = new InMemoryCartRepository();

        var productId = UUID.randomUUID();
        var userId = UUID.randomUUID();

        var inactiveProduct = new Product(
                productId,
                "Keyboard",
                new BigDecimal("150.00"),
                false
        );

        productRepository.save(inactiveProduct);

        var useCase = new AddItemToCartUseCase(
                productRepository,
                cartRepository
        );

        assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(userId, productId, 1)
        );
    }

    @Test
    void shouldIncreaseQuantityWhenSameProductIsAddedTwice() {
        var productRepository = new InMemoryProductRepository();
        var cartRepository = new InMemoryCartRepository();

        var productId = UUID.randomUUID();
        var userId = UUID.randomUUID();

        var product = new Product(
                productId,
                "Monitor",
                new BigDecimal("800.00"),
                true
        );

        productRepository.save(product);

        var useCase = new AddItemToCartUseCase(
                productRepository,
                cartRepository
        );

        useCase.execute(userId, productId, 1);
        useCase.execute(userId, productId, 2);

        var cart = cartRepository.findActiveByUserId(userId).orElseThrow();

        assertEquals(1, cart.items().size());
        assertEquals(3, cart.items().getFirst().quantity());
        assertEquals(new BigDecimal("2400.00"), cart.total());
    }

    @Test
    void shouldFailWhenQuantityIsInvalid() {
        var productRepository = new InMemoryProductRepository();
        var cartRepository = new InMemoryCartRepository();

        var productId = UUID.randomUUID();
        var userId = UUID.randomUUID();

        var product = new Product(
                productId,
                "Headset",
                new BigDecimal("300.00"),
                true
        );

        productRepository.save(product);

        var useCase = new AddItemToCartUseCase(
                productRepository,
                cartRepository
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(userId, productId, 0)
        );
    }
}

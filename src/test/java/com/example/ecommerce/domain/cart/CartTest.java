package com.example.ecommerce.domain.cart;
import com.example.ecommerce.application.security.CurrentUser;

import com.example.ecommerce.domain.exception.CartItemNotFoundException;
import com.example.ecommerce.domain.exception.InvalidQuantityException;
import com.example.ecommerce.domain.user.UserId;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

class CartTest {

    @Test
    void shouldRehydrateCart() {
        CartId cartId = new CartId("cart-1");
        UserId userId = new UserId("user-1"); // FIX: Define missing userId
        Map<ProductId, Integer> persistedItems = Map.of(new ProductId("p1"), 1);

        // FIX: Pass userId to the rehydrate method
        Cart cart = Cart.rehydrate(cartId, userId, persistedItems);
        
        assertEquals(userId, cart.getOwnerId());
    }

    @Test
    void shouldIncreaseQuantityWhenAddingSameProduct() {
        UserId userId = new UserId("user-1");
        Cart cart = new Cart(new CartId("cart-1"), userId);

        cart.addItem(new ProductId("product-1"), 2);
        cart.addItem(new ProductId("product-1"), 3);

        CartItem item = cart.getItems().iterator().next();
        assertEquals(5, item.getQuantity());
    }

    @Test
    void shouldThrowExceptionWhenAddingInvalidQuantity() {
        UserId userId = new UserId("user-1");
        Cart cart = new Cart(new CartId("cart-1"), userId);

        assertThrows(
                InvalidQuantityException.class,
                () -> cart.addItem(new ProductId("product-1"), 0)
        );
    }

    @Test
    void shouldUpdateItemQuantity() {
        UserId userId = new UserId("user-1");
        Cart cart = new Cart(new CartId("cart-1"), userId);
        ProductId pid = new ProductId("product-1");

        cart.addItem(pid, 2);
        cart.updateItemQuantity(pid, 5);

        CartItem item = cart.getItems().iterator().next();
        assertEquals(5, item.getQuantity());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingItem() {
        UserId userId = new UserId("user-1");
        Cart cart = new Cart(new CartId("cart-1"), userId);

        assertThrows(
                CartItemNotFoundException.class,
                () -> cart.updateItemQuantity(new ProductId("product-1"), 3)
        );
    }

    @Test
    void shouldRemoveItem() {
        UserId userId = new UserId("user-1");
        Cart cart = new Cart(new CartId("cart-1"), userId);
        ProductId pid = new ProductId("product-1");

        cart.addItem(pid, 2);
        cart.removeItem(pid);

        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void shouldThrowWhenRemovingNonExistingItem() {
        UserId userId = new UserId("user-1");
        Cart cart = new Cart(new CartId("cart-1"), userId);

        assertThrows(
                CartItemNotFoundException.class,
                () -> cart.removeItem(new ProductId("product-1"))
        );
    }

    @Test
    void shouldClearCart() {
        UserId userId = new UserId("user-1");
        Cart cart = new Cart(new CartId("cart-1"), userId);

        cart.addItem(new ProductId("product-1"), 2);
        cart.addItem(new ProductId("product-2"), 1);

        cart.clear();

        assertTrue(cart.isEmpty());
    }

    @Test
    void shouldRehydrateCartWithoutApplyingBusinessRules() {
        CartId cartId = new CartId("cart-1");

        Map<ProductId, Integer> persistedItems = Map.of(
                new ProductId("product-1"), 10,
                new ProductId("product-2"), 5
        );

        UserId userId = new UserId("user-1");
        Cart cart = Cart.rehydrate(cartId, userId, persistedItems);

        assertEquals(2, cart.getItems().size());
    }
}

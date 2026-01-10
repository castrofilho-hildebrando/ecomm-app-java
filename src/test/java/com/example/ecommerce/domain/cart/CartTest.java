package com.example.ecommerce.domain.cart;

import com.example.ecommerce.domain.exception.CartItemNotFoundException;
import com.example.ecommerce.domain.exception.InvalidQuantityException;
import com.example.ecommerce.domain.user.UserId;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    @Test
    void shouldAddItemToCart() {
        UserId userId = new UserId("user-1");
        CartId cartId = new CartId("cart-1");
        Cart cart = new Cart(cartId, userId);
        ProductId productId = new ProductId("product-1");

        cart.addItem(productId, 2);

        assertEquals(1, cart.getItems().size());
        CartItem item = cart.getItems().get(productId);
        assertEquals(2, item.getQuantity());
    }

    @Test
    void shouldIncreaseQuantityWhenAddingSameProduct() {
        UserId userId = new UserId("user-1");
        CartId cartId = new CartId("cart-1");
        Cart cart = new Cart(cartId, userId);
        ProductId productId = new ProductId("product-1");

        cart.addItem(productId, 2);
        cart.addItem(productId, 3);

        CartItem item = cart.getItems().get(productId);
        assertEquals(5, item.getQuantity());
    }

    @Test
    void shouldThrowExceptionWhenAddingInvalidQuantity() {
        UserId userId = new UserId("user-1");
        CartId cartId = new CartId("cart-1");
        Cart cart = new Cart(cartId, userId);

        assertThrows(
                InvalidQuantityException.class,
                () -> cart.addItem(new ProductId("product-1"), 0)
        );
    }

    @Test
    void shouldUpdateItemQuantity() {
        UserId userId = new UserId("user-1");
        CartId cartId = new CartId("cart-1");
        Cart cart = new Cart(cartId, userId);
        ProductId pid = new ProductId("product-1");

        cart.addItem(pid, 2);
        cart.updateItemQuantity(pid, 5);

        CartItem item = cart.getItems().get(pid);
        assertEquals(5, item.getQuantity());
    }

    @Test
    void shouldRemoveItemWhenQuantitySetToZero() {
        UserId userId = new UserId("user-1");
        CartId cartId = new CartId("cart-1");
        Cart cart = new Cart(cartId, userId);
        ProductId pid = new ProductId("product-1");

        cart.addItem(pid, 2);
        cart.updateItemQuantity(pid, 0);

        assertTrue(cart.isEmpty());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingItem() {
        UserId userId = new UserId("user-1");
        CartId cartId = new CartId("cart-1");
        Cart cart = new Cart(cartId, userId);

        assertThrows(
                CartItemNotFoundException.class,
                () -> cart.updateItemQuantity(new ProductId("product-1"), 3)
        );
    }

    @Test
    void shouldRemoveItem() {
        UserId userId = new UserId("user-1");
        CartId cartId = new CartId("cart-1");
        Cart cart = new Cart(cartId, userId);
        ProductId pid = new ProductId("product-1");

        cart.addItem(pid, 2);
        cart.removeItem(pid);

        assertTrue(cart.isEmpty());
    }

    @Test
    void shouldThrowWhenRemovingNonExistingItem() {
        UserId userId = new UserId("user-1");
        CartId cartId = new CartId("cart-1");
        Cart cart = new Cart(cartId, userId);

        assertThrows(
                CartItemNotFoundException.class,
                () -> cart.removeItem(new ProductId("product-1"))
        );
    }

    @Test
    void shouldClearCart() {
        UserId userId = new UserId("user-1");
        CartId cartId = new CartId("cart-1");
        Cart cart = new Cart(cartId, userId);

        cart.addItem(new ProductId("product-1"), 2);
        cart.addItem(new ProductId("product-2"), 3);

        cart.clear();

        assertTrue(cart.isEmpty());
    }

    @Test
    void shouldHaveCorrectOwner() {
        UserId userId = new UserId("user-1");
        CartId cartId = new CartId("cart-1");
        Cart cart = new Cart(cartId, userId);

        assertEquals(userId, cart.getOwnerId());
    }
}
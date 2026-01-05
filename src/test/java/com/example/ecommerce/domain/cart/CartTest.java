package com.example.ecommerce.domain.cart;
import com.example.ecommerce.application.security.CurrentUser;

import com.example.ecommerce.domain.exception.CartItemNotFoundException;
import com.example.ecommerce.domain.exception.InvalidQuantityException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

class CartTest {

    @Test
    void shouldAddNewItemToCart() {
        UserId uid = currentUser.id();
        Cart cart = new Cart(new CartId("cart-1", currentUser));

        cart.addItem(new ProductId("product-1"), 2);

        assertEquals(1, cart.getItems().size());
        CartItem item = cart.getItems().iterator().next();
        assertEquals("product-1", item.getProductId().value());
        assertEquals(2, item.getQuantity());
    }

    @Test
    void shouldIncreaseQuantityWhenAddingSameProduct() {
        UserId uid = currentUser.id();
        Cart cart = new Cart(new CartId("cart-1", currentUser));

        cart.addItem(new ProductId("product-1"), 2);
        cart.addItem(new ProductId("product-1"), 3);

        CartItem item = cart.getItems().iterator().next();
        assertEquals(5, item.getQuantity());
    }

    @Test
    void shouldThrowExceptionWhenAddingInvalidQuantity() {
        UserId uid = currentUser.id();
        Cart cart = new Cart(new CartId("cart-1"), currentUser);

        assertThrows(
                InvalidQuantityException.class,
                () -> cart.addItem(new ProductId("product-1"), 0)
        );
    }

    @Test
    void shouldUpdateItemQuantity() {
        UserId uid = currentUser.id();
        Cart cart = new Cart(new CartId("cart-1"), currentUser);
        ProductId pid = new ProductId("product-1");

        cart.addItem(pid, 2);
        cart.updateItemQuantity(pid, 5);

        CartItem item = cart.getItems().iterator().next();
        assertEquals(5, item.getQuantity());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingItem() {
        UserId uid = currentUser.id();
        Cart cart = new Cart(new CartId("cart-1"), currentUser);

        assertThrows(
                CartItemNotFoundException.class,
                () -> cart.updateItemQuantity(new ProductId("product-1"), 3)
        );
    }

    @Test
    void shouldRemoveItem() {
        UserId uid = currentUser.id();
        Cart cart = new Cart(new CartId("cart-1"), currentUser);
        ProductId pid = new ProductId("product-1");

        cart.addItem(pid, 2);
        cart.removeItem(pid);

        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void shouldThrowWhenRemovingNonExistingItem() {
        UserId uid = currentUser.id();
        Cart cart = new Cart(new CartId("cart-1"), currentUser);

        assertThrows(
                CartItemNotFoundException.class,
                () -> cart.removeItem(new ProductId("product-1"))
        );
    }

    @Test
    void shouldClearCart() {
        UserId uid = currentUser.id();
        Cart cart = new Cart(new CartId("cart-1"), currentUser);

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

        UserId uid = currentUser.id();
        Cart cart = Cart.rehydrate(cartId, userId, persistedItems);

        assertEquals(2, cart.getItems().size());
    }
}

package com.example.ecommerce.domain.cart;

import com.example.ecommerce.domain.exception.CartItemNotFoundException;
import com.example.ecommerce.domain.exception.InvalidQuantityException;
import com.example.ecommerce.domain.user.UserId;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class CartTest {

    private final UserId userId = new UserId("user-1");

    @Test
    void shouldAddNewItemToCart() {
        Cart cart = new Cart(new CartId("cart-1"), userId);

        cart.addItem(new ProductId("product-1"), 2);

        assertEquals(1, cart.getItems().size());
    }

    @Test
    void shouldIncreaseQuantityWhenAddingSameProduct() {
        Cart cart = new Cart(new CartId("cart-1"), userId);

        cart.addItem(new ProductId("product-1"), 2);
        cart.addItem(new ProductId("product-1"), 3);

        CartItem item = cart.getItems().get(0);
        assertEquals(5, item.getQuantity());
    }

    @Test
    void shouldThrowExceptionWhenAddingInvalidQuantity() {
        Cart cart = new Cart(new CartId("cart-1"), userId);

        assertThrows(
                InvalidQuantityException.class,
                () -> cart.addItem(new ProductId("product-1"), 0)
        );
    }

    @Test
    void shouldUpdateItemQuantity() {
        Cart cart = new Cart(new CartId("cart-1"), userId);
        ProductId pid = new ProductId("product-1");

        cart.addItem(pid, 2);
        cart.updateItemQuantity(pid, 5);

        CartItem item = cart.getItems().get(0);
        assertEquals(5, item.getQuantity());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingItem() {
        Cart cart = new Cart(new CartId("cart-1"), userId);

        assertThrows(
                CartItemNotFoundException.class,
                () -> cart.updateItemQuantity(new ProductId("product-1"), 3)
        );
    }

    @Test
    void shouldRemoveItem() {
        Cart cart = new Cart(new CartId("cart-1"), userId);
        ProductId pid = new ProductId("product-1");

        cart.addItem(pid, 2);
        cart.removeItem(pid);

        assertTrue(cart.isEmpty());
    }

    @Test
    void shouldThrowWhenRemovingNonExistingItem() {
        Cart cart = new Cart(new CartId("cart-1"), userId);

        assertThrows(
                CartItemNotFoundException.class,
                () -> cart.removeItem(new ProductId("product-1"))
        );
    }

    @Test
    void shouldClearCart() {
        Cart cart = new Cart(new CartId("cart-1"), userId);

        cart.addItem(new ProductId("product-1"), 2);
        cart.addItem(new ProductId("product-2"), 1);

        cart.clear();

        assertTrue(cart.isEmpty());
    }

    @Test
    void shouldRehydrateCartWithoutApplyingBusinessRules() {
        CartId cartId = new CartId("cart-1");

        List<CartItem> items = List.of(
                new CartItem(new ProductId("product-1"), 10),
                new CartItem(new ProductId("product-2"), 5)
        );

        Cart cart = Cart.rehydrate(cartId, userId, items);

        assertEquals(2, cart.getItems().size());
    }
}

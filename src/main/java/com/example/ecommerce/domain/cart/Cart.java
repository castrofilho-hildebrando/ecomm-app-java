package com.example.ecommerce.domain.cart;

import com.example.ecommerce.domain.user.UserId;
import com.example.ecommerce.domain.exception.InvalidQuantityException;
import com.example.ecommerce.domain.exception.CartItemNotFoundException;

import java.util.*;

public class Cart {

    private final CartId id;
    private final UserId ownerId;
    private final Map<ProductId, CartItem> items;

    public Cart(CartId id, UserId ownerId) {
            this.id = id;
            this.ownerId = ownerId;
            this.items = new HashMap<>();
    }

    public Cart(CartId id, UserId ownerId, Map<ProductId, CartItem> items) {
            this.id = id;
            this.ownerId = ownerId;
            this.items = new HashMap<>(items);
    }

    public CartId getId() {
        return id;
    }

    public UserId getOwnerId() {
        return ownerId;
    }

    public Collection<CartItem> getItems() {
        return Collections.unmodifiableCollection(items.values());
    }

    public void addItem(ProductId productId, int quantity) {
        if (quantity <= 0) {
            throw new InvalidQuantityException(quantity);
        }

        items.merge(
                productId,
                new CartItem(productId, quantity),
                CartItem::increase
        );
    }

    public void updateItemQuantity(ProductId productId, int quantity) {
        if (!items.containsKey(productId)) {
            throw new CartItemNotFoundException(productId.value());
        }

        if (quantity <= 0) {
            items.remove(productId);
        } else {
            items.put(productId, new CartItem(productId, quantity));
        }
    }

    public void removeItem(ProductId productId) {
        if (!items.containsKey(productId)) {
            throw new CartItemNotFoundException(productId.value());
        }
        items.remove(productId);
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public static Cart rehydrate(
            CartId id,
            UserId ownerId,
            Map<ProductId, Integer> persistedItems
    ) {
        Map<ProductId, CartItem> items = new HashMap<>();
        persistedItems.forEach(
                (pid, qty) -> items.put(pid, new CartItem(pid, qty))
        );
        return new Cart(id, ownerId, items);
    }
}

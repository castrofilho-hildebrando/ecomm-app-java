package com.example.ecommerce.domain.cart;

import java.util.*;

public class Cart {

    private final CartId id;
    private final Map<ProductId, CartItem> items;

    public Cart(CartId id) {
        this.id = id;
        this.items = new HashMap<>();
    }

    public CartId getId() {
        return id;
    }

    public Collection<CartItem> getItems() {
        return Collections.unmodifiableCollection(items.values());
    }

    public void addItem(ProductId productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        items.merge(
                productId,
                new CartItem(productId, quantity),
                CartItem::increase
        );
    }

    public void updateItemQuantity(ProductId productId, int quantity) {
        if (!items.containsKey(productId)) {
            throw new IllegalStateException("Item not found in cart");
        }

        if (quantity <= 0) {
            items.remove(productId);
        } else {
            items.put(productId, new CartItem(productId, quantity));
        }
    }

    public void removeItem(ProductId productId) {
        items.remove(productId);
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}

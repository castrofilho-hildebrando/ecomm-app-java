package com.example.ecommerce.domain.cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cart {

    private final UUID id;
    private final UUID userId;
    private final List<CartItem> items;

    public Cart(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User id is required");
        }
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.items = new ArrayList<>();
    }

    public UUID id() {
        return id;
    }

    public UUID userId() {
        return userId;
    }

    public List<CartItem> items() {
        return List.copyOf(items);
    }

    public void addItem(UUID productId, BigDecimal unitPrice, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        for (int i = 0; i < items.size(); i++) {
            CartItem existingItem = items.get(i);

            if (existingItem.productId().equals(productId)) {
                CartItem updatedItem = new CartItem(
                        productId,
                        unitPrice,
                        existingItem.quantity() + quantity
                );
                items.set(i, updatedItem);
                return;
            }
        }

        CartItem newItem = new CartItem(productId, unitPrice, quantity);
        items.add(newItem);
    }

    public void removeItem(UUID productId) {

        if (productId == null) {
            throw new IllegalArgumentException("Product id is required");
        }

        for (int i = 0; i < items.size(); i++) {
            CartItem existingItem = items.get(i);

            if (existingItem.productId().equals(productId)) {                
                items.remove(i);
                return;
            }
        }

        throw new IllegalStateException("Product not found in cart");
    }

    public void emptyCart() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public BigDecimal total() {
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : items) {
            total = total.add(item.subtotal());
        }

        return total;
    }
}

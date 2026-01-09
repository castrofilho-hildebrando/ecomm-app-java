package com.example.ecommerce.domain.cart;

import com.example.ecommerce.domain.exception.CartItemNotFoundException;
import com.example.ecommerce.domain.exception.EmptyCartException;
import com.example.ecommerce.domain.user.UserId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cart {

    private final CartId id;
    private final UserId ownerId;
    private final List<CartItem> items = new ArrayList<>();

    public Cart(CartId id, UserId ownerId) {
        this.id = id;
        this.ownerId = ownerId;
    }

    private Cart(CartId id, UserId ownerId, List<CartItem> items) {
        this.id = Objects.requireNonNull(id);
        this.ownerId = Objects.requireNonNull(ownerId);
        this.items.addAll(items);
    }

    public static Cart rehydrate(CartId id, UserId ownerId, List<CartItem> items) {
        return new Cart(id, ownerId, items);
    }

    public CartId getId() {
        return id;
    }

    public UserId getOwnerId() {
        return ownerId;
    }

    public List<CartItem> getItems() {
        return List.copyOf(items);
    }

    public void addItem(ProductId productId, int quantity) {
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            if (item.getProductId().equals(productId)) {
                items.set(i, item.increaseQuantity(quantity));
                return;
            }
        }
        items.add(new CartItem(productId, quantity));
    }

    public void updateItemQuantity(ProductId productId, int quantity) {
        CartItem item = findItem(productId);
        items.remove(item);
        items.add(new CartItem(productId, quantity));
    }

    public void removeItem(ProductId productId) {
        CartItem item = findItem(productId);
        items.remove(item);
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void validateNotEmpty() {
        if (items.isEmpty()) {
            throw new EmptyCartException(id.value());
        }
    }

    private CartItem findItem(ProductId productId) {
        return items.stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException(productId.value()));
    }
}

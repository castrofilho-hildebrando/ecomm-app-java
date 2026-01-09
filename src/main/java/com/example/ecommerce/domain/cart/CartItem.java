package com.example.ecommerce.domain.cart;

import com.example.ecommerce.domain.exception.InvalidQuantityException;

import java.util.Objects;

public class CartItem {

    private final ProductId productId;
    private final int quantity;

    public CartItem(ProductId productId, int quantity) {
        this.productId = Objects.requireNonNull(productId, "ProductId is required");
        if (quantity <= 0) {
            throw new InvalidQuantityException(quantity);
        }
        this.quantity = quantity;
    }

    public ProductId getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public CartItem increaseQuantity(int amount) {
        if (amount <= 0) {
            throw new InvalidQuantityException(amount);
        }
        return new CartItem(productId, this.quantity + amount);
    }

    public CartItem decreaseQuantity(int amount) {
        if (amount <= 0 || amount > this.quantity) {
            throw new InvalidQuantityException(amount);
        }
        return new CartItem(productId, this.quantity - amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem)) return false;
        CartItem that = (CartItem) o;
        return quantity == that.quantity &&
               productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, quantity);
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}

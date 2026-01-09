package com.example.ecommerce.domain.order;

import com.example.ecommerce.domain.cart.ProductId;
import com.example.ecommerce.domain.exception.InvalidQuantityException;

import java.util.Objects;

public class OrderItem {

    private final ProductId productId;
    private final int quantity;

    public OrderItem(ProductId productId, int quantity) {
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

    // Domain behavior examples
    public OrderItem increaseQuantity(int amount) {
        if (amount <= 0) {
            throw new InvalidQuantityException(amount);
        }
        return new OrderItem(productId, this.quantity + amount);
    }

    public OrderItem decreaseQuantity(int amount) {
        if (amount <= 0 || amount > this.quantity) {
            throw new InvalidQuantityException(amount);
        }
        return new OrderItem(productId, this.quantity - amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem)) return false;
        OrderItem that = (OrderItem) o;
        return quantity == that.quantity &&
               productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, quantity);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}

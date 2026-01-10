package com.example.ecommerce.domain.order;

import com.example.ecommerce.domain.exception.InvalidQuantityException;

import java.util.Objects;

public class OrderItem {

    private String productId;
    private int quantity;

    protected OrderItem() {
        // Required by Mongo
    }

    public OrderItem(OrderProductId productId, int quantity) {
        this.productId = Objects.requireNonNull(productId).value();
        if (quantity <= 0) {
            throw new InvalidQuantityException(quantity);
        }
        this.quantity = quantity;
    }

    public OrderProductId getProductId() {
        return new OrderProductId(productId);
    }

    public int getQuantity() {
        return quantity;
    }
}

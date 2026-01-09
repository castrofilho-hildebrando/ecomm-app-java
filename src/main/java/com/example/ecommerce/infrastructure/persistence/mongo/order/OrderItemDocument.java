package com.example.ecommerce.infrastructure.persistence.mongo.order;

import java.math.BigDecimal;

public class OrderItemDocument {

    private String productId;
    private BigDecimal price;
    private int quantity;

    public OrderItemDocument(String productId, BigDecimal price, int quantity) {
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}

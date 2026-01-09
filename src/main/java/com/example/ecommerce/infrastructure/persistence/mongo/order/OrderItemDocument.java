package com.example.ecommerce.infrastructure.persistence.mongo.order;

public class OrderItemDocument {

    private String productId;
    private int quantity;

    public OrderItemDocument(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}

package com.example.ecommerce.domain.order;

import com.example.ecommerce.domain.cart.ProductId;

public class OrderItem {

    private final ProductId productId;
    private final int quantity;

    public OrderItem(ProductId productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public ProductId getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}

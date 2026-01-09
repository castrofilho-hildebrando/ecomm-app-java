package com.example.ecommerce.domain.order;

import com.example.ecommerce.domain.cart.ProductId;

import java.math.BigDecimal;

public class OrderItem {

    private final ProductId productId;
    private final BigDecimal price;
    private final int quantity;

    public OrderItem(ProductId productId, BigDecimal price, int quantity) {
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }

    public ProductId getProductId() {
        return productId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal total() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}

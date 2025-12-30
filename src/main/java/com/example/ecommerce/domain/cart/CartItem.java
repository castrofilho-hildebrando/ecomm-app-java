package com.example.ecommerce.domain.cart;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItem(
        UUID productId,
        BigDecimal unitPrice,
        int quantity
) {

    public CartItem {
        if (productId == null) {
            throw new IllegalArgumentException("Product id is required");
        }
        if (unitPrice == null || unitPrice.signum() <= 0) {
            throw new IllegalArgumentException("Unit price must be positive");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }

    public BigDecimal subtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}

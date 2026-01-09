package com.example.ecommerce.domain.cart;

public class CartItem {

    private final ProductId productId;
    private final int quantity;

    public CartItem(ProductId productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public ProductId getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    CartItem increase(CartItem other) {
        return new CartItem(productId, this.quantity + other.quantity);
    }
}

package com.example.ecommerce.domain.cart;

import java.util.Objects;

public record ProductId(String value) {
    public ProductId {
        Objects.requireNonNull(value);
    }
}

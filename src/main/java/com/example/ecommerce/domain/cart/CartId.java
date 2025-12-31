package com.example.ecommerce.domain.cart;

import java.util.Objects;

public record CartId(String value) {
    public CartId {
        Objects.requireNonNull(value);
    }
}

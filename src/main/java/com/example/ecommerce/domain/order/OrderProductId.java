package com.example.ecommerce.domain.order;

import java.util.Objects;

public record OrderProductId(String value) {
    public OrderProductId {
        Objects.requireNonNull(value);
    }
}

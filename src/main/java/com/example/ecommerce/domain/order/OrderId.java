package com.example.ecommerce.domain.order;

import java.util.Objects;
import java.util.UUID;

public record OrderId(String value) {

    public OrderId {
        Objects.requireNonNull(value);
    }

    public static OrderId newId() {
        return new OrderId(UUID.randomUUID().toString());
    }
}


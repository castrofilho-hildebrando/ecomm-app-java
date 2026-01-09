package com.example.ecommerce.domain.user;

import java.util.Objects;
import java.util.UUID;

public record UserId(String value) {

    public UserId {
        Objects.requireNonNull(value);
    }

    public static UserId newId() {
        return new UserId(UUID.randomUUID().toString());
    }
}

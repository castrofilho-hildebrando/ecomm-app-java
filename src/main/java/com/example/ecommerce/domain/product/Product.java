package com.example.ecommerce.domain.product;

import com.example.ecommerce.domain.exception.ProductIdIsRequiredException;
import com.example.ecommerce.domain.exception.ProductNameIsRequiredException;
import com.example.ecommerce.domain.exception.ProductPriceMustBePositiveException;

import java.math.BigDecimal;
import java.util.UUID;

public final class Product {

    private final UUID id;
    private final String name;
    private final BigDecimal price;
    private final boolean active;

    public Product(
            UUID id,
            String name,
            BigDecimal price,
            boolean active
    ) {
        if (id == null) {
            throw new ProductIdIsRequiredException();
        }
        if (name == null || name.isBlank()) {
            throw new ProductNameIsRequiredException();
        }
        if (price == null || price.signum() <= 0) {
            throw new ProductPriceMustBePositiveException();
        }

        this.id = id;
        this.name = name;
        this.price = price;
        this.active = active;
    }

    public UUID id() {
        return id;
    }

    public String name() {
        return name;
    }

    public BigDecimal price() {
        return price;
    }

    public boolean isActive() {
        return active;
    }
}

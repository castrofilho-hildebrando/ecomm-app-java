package com.example.ecommerce.domain.product;

import java.math.BigDecimal;

public record ProductSnapshot(
        String productId,
        String name,
        BigDecimal price
) {}

package com.example.ecommerce.infrastructure.web.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record AddItemRequest(

        @NotBlank
        String productId,

        @Min(1)
        int quantity
) {}

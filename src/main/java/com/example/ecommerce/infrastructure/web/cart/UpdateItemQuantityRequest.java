package com.example.ecommerce.infrastructure.web.cart;

import jakarta.validation.constraints.Min;

public record UpdateItemQuantityRequest(
        @Min(0)
        int quantity
) {}

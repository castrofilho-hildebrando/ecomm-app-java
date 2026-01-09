package com.example.ecommerce.infrastructure.product;

import com.example.ecommerce.application.product.ProductGateway;
import com.example.ecommerce.domain.cart.ProductId;
import com.example.ecommerce.domain.product.ProductSnapshot;

import java.math.BigDecimal;

public class InMemoryProductGateway implements ProductGateway {

    @Override
    public ProductSnapshot getProduct(ProductId productId) {
        return new ProductSnapshot(
                productId.value(),
                "Fake Product",
                BigDecimal.valueOf(100)
        );
    }
}

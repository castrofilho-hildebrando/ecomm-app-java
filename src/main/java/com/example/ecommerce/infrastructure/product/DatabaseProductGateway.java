package com.example.ecommerce.infrastructure.product;

import com.example.ecommerce.application.product.ProductGateway;
import com.example.ecommerce.domain.cart.ProductId;
import com.example.ecommerce.domain.product.ProductRepository;
import com.example.ecommerce.domain.product.ProductSnapshot;

public class DatabaseProductGateway implements ProductGateway {

    private final ProductRepository repository;

    public DatabaseProductGateway(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductSnapshot getProduct(ProductId productId) {
        return repository.findById(java.util.UUID.fromString(productId.value()))
                .map(p -> new ProductSnapshot(
                        p.id().toString(),
                        p.name(),
                        p.price()
                ))
                .orElseThrow();
    }
}

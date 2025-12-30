package com.example.ecommerce.application.cart;

import com.example.ecommerce.domain.product.Product;
import com.example.ecommerce.domain.product.ProductRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

class InMemoryProductRepository implements ProductRepository {

    private final Map<UUID, Product> products = new HashMap<>();

    void save(Product product) {
        products.put(product.id(), product);
    }

    @Override
    public Optional<Product> findById(UUID productId) {
        return Optional.ofNullable(products.get(productId));
    }
}

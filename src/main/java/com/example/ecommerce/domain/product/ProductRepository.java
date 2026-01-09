package com.example.ecommerce.domain.product;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Optional<Product> findById(UUID productId);
}

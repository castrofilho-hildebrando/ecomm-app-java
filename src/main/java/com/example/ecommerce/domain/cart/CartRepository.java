package com.example.ecommerce.domain.cart;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository {

    Optional<Cart> findActiveByUserId(UUID userId);

    void save(Cart cart);
}

package com.example.ecommerce.domain.cart;

import java.util.Optional;

public interface CartRepository {

    Optional<Cart> findById(CartId id);

    void save(Cart cart);
    void delete(CartId id);
}

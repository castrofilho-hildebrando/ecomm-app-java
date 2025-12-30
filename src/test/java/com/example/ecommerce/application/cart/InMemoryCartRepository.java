package com.example.ecommerce.application.cart;

import com.example.ecommerce.domain.cart.Cart;
import com.example.ecommerce.domain.cart.CartRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

class InMemoryCartRepository implements CartRepository {

    private final Map<UUID, Cart> cartsByUser = new HashMap<>();

    @Override
    public Optional<Cart> findActiveByUserId(UUID userId) {
        return Optional.ofNullable(cartsByUser.get(userId));
    }

    @Override
    public void save(Cart cart) {
        cartsByUser.put(cart.userId(), cart);
    }
}

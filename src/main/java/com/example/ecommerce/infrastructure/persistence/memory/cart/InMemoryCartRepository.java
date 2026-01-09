package com.example.ecommerce.infrastructure.persistence.memory.cart;

import com.example.ecommerce.domain.cart.Cart;
import com.example.ecommerce.domain.cart.CartId;
import com.example.ecommerce.domain.cart.CartRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCartRepository implements CartRepository {

    private final Map<String, Cart> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<Cart> findById(CartId id) {
        return Optional.ofNullable(storage.get(id.value()));
    }

    @Override
    public void save(Cart cart) {
        storage.put(cart.getId().value(), cart);
    }

    @Override
    public void delete(CartId id) {
        storage.remove(id.value());
    }

    public void clear() {
        this.storage.clear();
    }
}
package com.example.ecommerce.infrastructure.persistence.mongo.cart;

import com.example.ecommerce.domain.cart.Cart;
import com.example.ecommerce.domain.cart.CartId;
import com.example.ecommerce.domain.cart.CartRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MongoCartRepository implements CartRepository {

    private final SpringDataCartRepository repository;

    public MongoCartRepository(SpringDataCartRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Cart> findById(CartId id) {
        return repository.findById(id.value());
    }

    @Override
    public void save(Cart cart) {
        repository.save(cart);
    }

    @Override
    public void delete(CartId id) {
        repository.deleteById(id.value());
    }
}

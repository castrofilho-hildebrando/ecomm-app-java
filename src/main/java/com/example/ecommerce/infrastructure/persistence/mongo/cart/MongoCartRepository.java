package com.example.ecommerce.infrastructure.persistence.mongo.cart;

import com.example.ecommerce.domain.cart.Cart;
import com.example.ecommerce.domain.cart.CartId;
import com.example.ecommerce.domain.cart.CartRepository;
import com.example.ecommerce.infrastructure.mapper.CartMapper;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MongoCartRepository implements CartRepository {

    private final SpringDataCartRepository repository;

    public MongoCartRepository(SpringDataCartRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public Optional<Cart> findById(CartId id) {
        return repository.findById(id.value())
                .map(CartMapper::toDomain);
    }

    @Override
    public void save(Cart cart) {
        repository.save(CartMapper.toDocument(cart));
    }

    @Override
    public void delete(Cart cart) {
        springDataCartRepository.deleteById(cart.value());
    }
}

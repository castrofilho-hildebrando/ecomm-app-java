package com.example.ecommerce.infrastructure.persistence.mongo.cart;

import com.example.ecommerce.domain.cart.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataCartRepository
        extends MongoRepository<Cart, String> {
}

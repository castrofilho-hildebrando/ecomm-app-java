package com.example.ecommerce.infrastructure.persistence.mongo.cart;

import com.example.ecommerce.infrastructure.persistence.mongo.cart.CartDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataCartRepository
        extends MongoRepository<CartDocument, String> {
}

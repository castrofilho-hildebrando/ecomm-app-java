package com.example.ecommerce.infrastructure.persistence.mongo.cart;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataCartRepository
        extends MongoRepository<CartDocument, String> {
}

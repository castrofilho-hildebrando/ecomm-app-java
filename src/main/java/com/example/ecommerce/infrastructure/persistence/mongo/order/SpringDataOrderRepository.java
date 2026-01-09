package com.example.ecommerce.infrastructure.persistence.mongo.order;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataOrderRepository extends MongoRepository<OrderDocument, String> {
    // Spring Data will generate queries automatically
}

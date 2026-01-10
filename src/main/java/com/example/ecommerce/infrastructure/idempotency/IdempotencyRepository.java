package com.example.ecommerce.infrastructure.idempotency;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface IdempotencyRepository
        extends MongoRepository<IdempotencyKey, String> {
}

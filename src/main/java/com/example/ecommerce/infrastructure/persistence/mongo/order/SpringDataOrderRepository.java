package com.example.ecommerce.infrastructure.persistence.mongo.order;

import com.example.ecommerce.domain.order.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataOrderRepository
        extends MongoRepository<Order, String> {
}

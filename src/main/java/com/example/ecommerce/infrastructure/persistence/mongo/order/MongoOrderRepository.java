package com.example.ecommerce.infrastructure.persistence.mongo.order;

import com.example.ecommerce.domain.order.Order;
import com.example.ecommerce.domain.order.OrderId;
import com.example.ecommerce.domain.order.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MongoOrderRepository implements OrderRepository {

    private final SpringDataOrderRepository repository;

    public MongoOrderRepository(SpringDataOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Order> findById(OrderId id) {
        return repository.findById(id.value());
    }

    @Override
    public void save(Order order) {
        repository.save(order);
    }

    @Override
    public void delete(OrderId id) {
        repository.deleteById(id.value());
    }
}

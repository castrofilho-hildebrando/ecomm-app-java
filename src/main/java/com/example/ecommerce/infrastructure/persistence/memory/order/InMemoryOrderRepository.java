package com.example.ecommerce.infrastructure.persistence.memory.order;

import com.example.ecommerce.domain.order.Order;
import com.example.ecommerce.domain.order.OrderId;
import com.example.ecommerce.domain.order.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryOrderRepository implements OrderRepository {

    private final Map<String, Order> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<Order> findById(OrderId id) {
        return Optional.ofNullable(storage.get(id.value()));
    }

    @Override
    public void save(Order order) {
        storage.put(order.getId().value(), order);
    }

    @Override
    public void delete(OrderId id) {
        storage.remove(id.value());
    }

    // test/support convenience
    public void clear() {
        storage.clear();
    }
}

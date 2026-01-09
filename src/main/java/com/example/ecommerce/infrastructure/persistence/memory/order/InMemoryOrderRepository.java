package com.example.ecommerce.infrastructure.persistence.memory.order;

import com.example.ecommerce.domain.order.Order;
import com.example.ecommerce.domain.order.OrderId;
import com.example.ecommerce.domain.order.OrderRepository;

import java.util.*;

public class InMemoryOrderRepository implements OrderRepository {

    private final Map<OrderId, Order> storage = new HashMap<>();

    @Override
    public void save(Order order) {
        storage.put(order.getId(), order);
    }

    @Override
    public Optional<Order> findById(OrderId id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void delete(OrderId id) {
        storage.remove(id);
    }

    // TEST SUPPORT ONLY
    public List<Order> findAll() {
        return new ArrayList<>(storage.values());
    }

    // TEST SUPPORT ONLY
    public void clear() {
        storage.clear();
    }
}

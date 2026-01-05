package com.example.ecommerce.domain.order;

import java.util.Optional;

public interface OrderRepository {

    Optional<Order> findById(OrderId id);

    void save(Order order);

    void delete(OrderId id);
}

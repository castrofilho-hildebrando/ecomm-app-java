package com.example.ecommerce.infrastructure.persistence.mongo.order;

import com.example.ecommerce.domain.order.Order;
import com.example.ecommerce.domain.order.OrderId;
import com.example.ecommerce.domain.order.OrderRepository;
import com.example.ecommerce.infrastructure.mapper.OrderMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MongoOrderRepository implements OrderRepository {

    private final SpringDataOrderRepository springDataOrderRepository;

    public MongoOrderRepository(SpringDataOrderRepository springDataOrderRepository) {
        this.springDataOrderRepository = springDataOrderRepository;
    }

    @Override
    public Optional<Order> findById(OrderId id) {
        return springDataOrderRepository.findById(id.value())
                .map(OrderMapper::toDomain);
    }

    @Override
    public void save(Order order) {
        springDataOrderRepository.save(OrderMapper.toDocument(order));
    }

    @Override
    public void delete(OrderId id) {
        springDataOrderRepository.deleteById(id.value());
    }
}

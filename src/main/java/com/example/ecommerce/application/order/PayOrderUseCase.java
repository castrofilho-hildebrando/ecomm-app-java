package com.example.ecommerce.application.order;

import com.example.ecommerce.application.event.DomainEventPublisher;
import com.example.ecommerce.application.security.CurrentUser;
import com.example.ecommerce.domain.exception.OrderNotFoundException;
import com.example.ecommerce.domain.order.Order;
import com.example.ecommerce.domain.order.OrderId;
import com.example.ecommerce.domain.order.OrderRepository;
import com.example.ecommerce.infrastructure.idempotency.IdempotencyKey;
import com.example.ecommerce.infrastructure.idempotency.IdempotencyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;

public class PayOrderUseCase {

    private final OrderRepository orderRepository;
    private final IdempotencyRepository idempotencyRepository;
    private final DomainEventPublisher publisher;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PayOrderUseCase(
            OrderRepository orderRepository,
            IdempotencyRepository idempotencyRepository,
            DomainEventPublisher publisher
    ) {
        this.orderRepository = orderRepository;
        this.idempotencyRepository = idempotencyRepository;
        this.publisher = publisher;
    }

    @Transactional
    public OrderView execute(
            String orderId,
            CurrentUser currentUser,
            String idempotencyKey
    ) {

        return idempotencyRepository.findById(idempotencyKey)
                .map(this::deserialize)
                .orElseGet(() -> process(orderId, idempotencyKey));
    }

    private OrderView process(String orderId, String idempotencyKey) {
        Order order = orderRepository.findById(new OrderId(orderId))
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.markAsPaid();
        orderRepository.save(order);
        order.pullDomainEvents().forEach(publisher::publish);

        OrderView view = OrderView.from(order);

        persistKey(idempotencyKey, view);

        return view;
    }

    private void persistKey(String key, OrderView response) {
        try {
            String json = objectMapper.writeValueAsString(response);
            idempotencyRepository.save(
                    new IdempotencyKey(
                            key,
                            "PayOrder",
                            json
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to persist idempotency key", e);
        }
    }

    private OrderView deserialize(IdempotencyKey key) {
        try {
            return objectMapper.readValue(
                    key.getResponse(),
                    OrderView.class
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize idempotent response", e);
        }
    }
}

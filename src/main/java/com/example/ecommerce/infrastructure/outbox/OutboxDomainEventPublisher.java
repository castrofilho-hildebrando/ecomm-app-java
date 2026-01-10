package com.example.ecommerce.infrastructure.outbox;

import com.example.ecommerce.application.event.DomainEventPublisher;
import com.example.ecommerce.domain.event.DomainEvent;
import org.springframework.transaction.annotation.Transactional;

public class OutboxDomainEventPublisher implements DomainEventPublisher {

    private final OutboxRepository repository;
    private final DomainEventSerializer serializer;

    public OutboxDomainEventPublisher(
            OutboxRepository repository,
            DomainEventSerializer serializer
    ) {
        this.repository = repository;
        this.serializer = serializer;
    }

    @Override
    @Transactional
    public void publish(DomainEvent event) {
        OutboxEvent outbox = new OutboxEvent(
                event.getClass().getSimpleName(),
                extractAggregateId(event),
                event.getClass().getName(),
                serializer.serialize(event)
        );

        repository.save(outbox);
    }

    private String extractAggregateId(DomainEvent event) {
        try {
            return event.getClass()
                    .getMethod("orderId")
                    .invoke(event)
                    .toString();
        } catch (Exception e) {
            return "unknown";
        }
    }
}

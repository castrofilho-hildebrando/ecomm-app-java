package com.example.ecommerce.infrastructure.outbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class OutboxEventPublisher {

    private static final Logger log =
            LoggerFactory.getLogger(OutboxEventPublisher.class);

    private final OutboxRepository repository;

    public OutboxEventPublisher(OutboxRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedDelay = 3000)
    @Transactional
    public void publishPendingEvents() {
        List<OutboxEvent> events =
                repository.findByPublishedFalse();

        for (OutboxEvent event : events) {
            // Aqui você publicaria no Kafka, RabbitMQ, etc.
            log.info("Publishing event: {}", event.getEventType());

            event.markPublished();
            repository.save(event);
        }
    }
}

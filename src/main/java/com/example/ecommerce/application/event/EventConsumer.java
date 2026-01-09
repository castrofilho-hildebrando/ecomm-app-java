package com.example.ecommerce.application.event;

import com.example.ecommerce.domain.event.DomainEvent;

public class EventConsumer {

    private final DomainEventPublisher publisher;

    public EventConsumer(DomainEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void accept(DomainEvent event) {
        publisher.publish(event);
    }
}

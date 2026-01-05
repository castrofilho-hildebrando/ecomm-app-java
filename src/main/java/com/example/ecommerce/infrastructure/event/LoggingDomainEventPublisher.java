package com.example.ecommerce.infrastructure.event;

import com.example.ecommerce.application.event.DomainEventPublisher;
import com.example.ecommerce.domain.event.DomainEvent;

public class LoggingDomainEventPublisher
        implements DomainEventPublisher {

    @Override
    public void publish(DomainEvent event) {
        System.out.println(
                "Domain event published: " + event
        );
    }
}

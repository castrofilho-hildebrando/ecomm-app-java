package com.example.ecommerce.application.event;

import com.example.ecommerce.domain.event.DomainEvent;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}

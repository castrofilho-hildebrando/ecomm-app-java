package com.example.ecommerce.infrastructure.outbox;

import com.example.ecommerce.domain.event.DomainEvent;

public interface DomainEventSerializer {
    String serialize(DomainEvent event);
}

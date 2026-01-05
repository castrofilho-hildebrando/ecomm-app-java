package com.example.ecommerce.domain.event;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredAt();
}

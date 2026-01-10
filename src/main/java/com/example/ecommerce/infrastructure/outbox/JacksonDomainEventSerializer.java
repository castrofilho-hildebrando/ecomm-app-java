package com.example.ecommerce.infrastructure.outbox;

import com.example.ecommerce.domain.event.DomainEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonDomainEventSerializer
        implements DomainEventSerializer {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String serialize(DomainEvent event) {
        try {
            return mapper.writeValueAsString(event);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
    }
}

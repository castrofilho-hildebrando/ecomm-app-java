package com.example.ecommerce.application.order;

import com.example.ecommerce.application.event.DomainEventPublisher;
import com.example.ecommerce.domain.event.DomainEvent;

import java.util.List;
import java.util.ArrayList;

class FakeDomainEventPublisher implements DomainEventPublisher {

    private final List<DomainEvent> events = new ArrayList<>();

    @Override
    public void publish(DomainEvent event) {
        events.add(event);
    }

    int count() {
        return events.size();
    }

    DomainEvent first() {
        return events.get(0);
    }
}

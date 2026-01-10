package com.example.ecommerce.infrastructure.outbox;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Document(collection = "outbox_events")
public class OutboxEvent {

    @Id
    private String id;

    private String aggregateType;
    private String aggregateId;
    private String eventType;
    private String payload;
    private Instant occurredAt;
    private boolean published;

    protected OutboxEvent() {}

    public OutboxEvent(
            String aggregateType,
            String aggregateId,
            String eventType,
            String payload
    ) {
        this.id = UUID.randomUUID().toString();
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.occurredAt = Instant.now();
        this.published = false;
    }

    public String getId() {
        return id;
    }

    public boolean isPublished() {
        return published;
    }

    public void markPublished() {
        this.published = true;
    }

    public String getPayload() {
        return payload;
    }

    public String getEventType() {
        return eventType;
    }
}

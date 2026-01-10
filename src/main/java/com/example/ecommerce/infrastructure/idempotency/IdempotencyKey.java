package com.example.ecommerce.infrastructure.idempotency;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "idempotency_keys")
public class IdempotencyKey {

    @Id
    private String key;

    private String command;
    private String response;
    private Instant createdAt;

    protected IdempotencyKey() {
    }

    public IdempotencyKey(String key, String command, String response) {
        this.key = key;
        this.command = command;
        this.response = response;
        this.createdAt = Instant.now();
    }

    public String getKey() {
        return key;
    }

    public String getResponse() {
        return response;
    }
}

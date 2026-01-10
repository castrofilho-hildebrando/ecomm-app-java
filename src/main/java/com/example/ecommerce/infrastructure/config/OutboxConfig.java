package com.example.ecommerce.infrastructure.config;

import com.example.ecommerce.application.event.DomainEventPublisher;
import com.example.ecommerce.infrastructure.outbox.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class OutboxConfig {

    @Bean
    DomainEventSerializer domainEventSerializer() {
        return new JacksonDomainEventSerializer();
    }

    @Bean
    DomainEventPublisher domainEventPublisher(
            OutboxRepository repository,
            DomainEventSerializer serializer
    ) {
        return new OutboxDomainEventPublisher(repository, serializer);
    }

    @Bean
    OutboxEventPublisher outboxEventPublisher(
            OutboxRepository repository
    ) {
        return new OutboxEventPublisher(repository);
    }
}

package com.example.ecommerce.infrastructure.config;

import com.example.ecommerce.application.event.DomainEventPublisher;
import com.example.ecommerce.application.order.PayOrderUseCase;
import com.example.ecommerce.domain.order.OrderRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.ecommerce.application.order.GetOrderUseCase;

@Configuration
public class OrderConfig {

    @Bean
    public PayOrderUseCase payOrderUseCase(
            OrderRepository orderRepository,
            DomainEventPublisher publisher
    ) {
        return new PayOrderUseCase(orderRepository, publisher);
    }

    @Bean
    public GetOrderUseCase getOrderUseCase(
            OrderRepository orderRepository
    ) {
        return new GetOrderUseCase(orderRepository);
    }
}

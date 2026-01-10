package com.example.ecommerce.infrastructure.config;

import com.example.ecommerce.application.order.PayOrderUseCase;
import com.example.ecommerce.application.order.PlaceOrderFromCartUseCase;
import com.example.ecommerce.domain.cart.CartRepository;
import com.example.ecommerce.domain.order.OrderRepository;
import com.example.ecommerce.application.event.DomainEventPublisher;
import com.example.ecommerce.infrastructure.idempotency.IdempotencyRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfig {

    @Bean
    PayOrderUseCase payOrderUseCase(
            OrderRepository orderRepository,
            IdempotencyRepository idempotencyRepository,
            DomainEventPublisher publisher
    ) {
        return new PayOrderUseCase(
                orderRepository,
                idempotencyRepository,
                publisher
        );
    }

    @Bean
    PlaceOrderFromCartUseCase placeOrderFromCartUseCase(
            CartRepository cartRepository,
            OrderRepository orderRepository,
            DomainEventPublisher publisher
    ) {
        return new PlaceOrderFromCartUseCase(
                cartRepository,
                orderRepository,
                publisher
        );
    }
}

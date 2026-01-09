package com.example.ecommerce.infrastructure.config;

import com.example.ecommerce.application.event.DomainEventPublisher;
import com.example.ecommerce.application.order.PlaceOrderFromCartUseCase;
import com.example.ecommerce.application.product.ProductGateway;
import com.example.ecommerce.domain.cart.CartRepository;
import com.example.ecommerce.domain.order.OrderRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfig {

    @Bean
    PlaceOrderFromCartUseCase placeOrderFromCartUseCase(
            CartRepository cartRepository,
            OrderRepository orderRepository,
            ProductGateway productGateway,
            DomainEventPublisher eventPublisher
    ) {
        return new PlaceOrderFromCartUseCase(
                cartRepository,
                orderRepository,
                productGateway,
                eventPublisher
        );
    }
}

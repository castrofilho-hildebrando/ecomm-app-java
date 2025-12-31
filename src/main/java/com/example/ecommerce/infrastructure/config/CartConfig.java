package com.example.ecommerce.infrastructure.config;

import com.example.ecommerce.application.cart.AddItemToCartUseCase;
import com.example.ecommerce.domain.cart.CartRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CartConfig {

    @Bean
    public AddItemToCartUseCase addItemToCartUseCase(
            CartRepository cartRepository
    ) {
        return new AddItemToCartUseCase(cartRepository);
    }
}

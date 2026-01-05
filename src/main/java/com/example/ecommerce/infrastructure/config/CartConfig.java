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

    @Bean
    RemoveItemFromCartUseCase removeItemFromCartUseCase(CartRepository repo) {
        return new RemoveItemFromCartUseCase(repo);
    }

    @Bean
    UpdateCartItemQuantityUseCase updateCartItemQuantityUseCase(CartRepository repo) {
        return new UpdateCartItemQuantityUseCase(repo);
    }

    @Bean
    ClearCartUseCase clearCartUseCase(CartRepository repo) {
        return new ClearCartUseCase(repo);
    }

    @Bean
    GetCartUseCase getCartUseCase(CartRepository repo) {
        return new GetCartUseCase(repo);
    }
}

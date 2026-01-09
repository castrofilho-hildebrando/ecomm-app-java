package com.example.ecommerce.infrastructure.product;

import com.example.ecommerce.application.product.ProductGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductConfig {

    @Bean
    ProductGateway productGateway() {
        return new InMemoryProductGateway();
    }
}

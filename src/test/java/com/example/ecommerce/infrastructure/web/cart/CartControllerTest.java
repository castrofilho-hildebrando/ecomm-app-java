package com.example.ecommerce.infrastructure.web.cart;

import com.example.ecommerce.EcommerceApplication;
import org.springframework.test.context.ContextConfiguration;
import com.example.ecommerce.application.cart.*;
import com.example.ecommerce.domain.cart.CartRepository;
import com.example.ecommerce.infrastructure.persistence.memory.cart.InMemoryCartRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CartController.class)
@ContextConfiguration(classes = EcommerceApplication.class)
@Import(CartControllerTest.TestConfig.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnEmptyCartWhenCartExistsButHasNoItems() throws Exception {
        mockMvc.perform(get("/carts/cart-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value("cart-1"))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items").isEmpty());
    }

    static class TestConfig {

        @Bean
        CartRepository cartRepository() {
            return new InMemoryCartRepository();
        }

        @Bean
        AddItemToCartUseCase addItemToCartUseCase(CartRepository repo) {
            return new AddItemToCartUseCase(repo);
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
}

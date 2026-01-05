    package com.example.ecommerce.infrastructure.web.cart;

    import com.example.ecommerce.EcommerceApplication;
    import com.example.ecommerce.application.cart.*;
    import com.example.ecommerce.domain.cart.CartRepository;
    import com.example.ecommerce.infrastructure.persistence.memory.cart.InMemoryCartRepository;

    import org.springframework.test.context.ContextConfiguration;
    import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Import;
    import org.springframework.beans.factory.annotation.Autowired;

    import org.springframework.test.web.servlet.MockMvc;
    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

    import org.junit.jupiter.api.Test;

    @WebMvcTest(controllers = CartController.class)
    @Import(CartControllerTest.TestConfig.class)
    class CartControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Test
        void shouldAddItemToCart() throws Exception {
            mockMvc.perform(post("/carts/cart-add/items")
                   .contentType("application/json")
                    .content("""
                        {
                            "productId": "product-1",
                            "quantity": 2
                        }
                    """))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/carts/cart-add"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.items[0].productId").value("product-1"))
                    .andExpect(jsonPath("$.items[0].quantity").value(2));
        }

        @Test
        void shouldUpdateItemQuantity() throws Exception {
            mockMvc.perform(post("/carts/cart-update/items")
                    .contentType("application/json")
                    .content("""
                        {
                            "productId": "product-1",
                            "quantity": 2
                        }
                    """));

            mockMvc.perform(put("/carts/cart-update/items/product-1")
                    .contentType("application/json")
                    .content("""
                        {
                            "quantity": 5
                        }
                    """))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/carts/cart-update"))
                    .andExpect(jsonPath("$.items[0].quantity").value(5));
        }

        @Test
        void shouldClearCart() throws Exception {
            mockMvc.perform(post("/carts/cart-clear/items")
                    .contentType("application/json")
                    .content("""
                        {
                            "productId": "product-1",
                            "quantity": 2
                        }
                    """));

            mockMvc.perform(delete("/carts/cart-clear"))
                        .andExpect(status().isNoContent());

            mockMvc.perform(get("/carts/cart-clear"))
                    .andExpect(jsonPath("$.items").isEmpty());
        }

        @Test
        void shouldReturn404WhenUpdatingNonExistingCart() throws Exception {
            mockMvc.perform(put("/carts/cart-404/items/product-1")
                    .contentType("application/json")
                    .content("""
                        { "quantity": 1 }
                    """))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        void shouldRemoveItemFromCart() throws Exception {
            mockMvc.perform(post("/carts/cart-remove-item/items")
                    .contentType("application/json")
                    .content("""
                        {
                            "productId": "product-1",
                            "quantity": 2
                        }
                    """));

            mockMvc.perform(delete("/carts/cart-remove-item/items/product-1"))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/carts/cart-remove-item"))
                    .andExpect(jsonPath("$.items").isEmpty());
        }

        @Test
        void shouldReturnEmptyCartWhenCartExistsButHasNoItems() throws Exception {
            mockMvc.perform(get("/carts/cart-noitems"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.cartId").value("cart-noitems"))
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

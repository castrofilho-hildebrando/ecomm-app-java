package com.example.ecommerce.infrastructure.web.cart;

import com.example.ecommerce.application.cart.*;
import com.example.ecommerce.domain.cart.Cart;
import com.example.ecommerce.domain.cart.CartId;
import com.example.ecommerce.domain.cart.CartRepository;
import com.example.ecommerce.domain.cart.ProductId;
import com.example.ecommerce.domain.user.UserId;
import com.example.ecommerce.infrastructure.persistence.memory.cart.InMemoryCartRepository;
import com.example.ecommerce.infrastructure.web.ApiExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

// Importante: Adicione este import para o CSRF
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CartController.class)
@Import(CartControllerTest.TestConfig.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartRepository cartRepository;

    private final String userId = "user-1";

    @BeforeEach
    void setUp() {
        ((InMemoryCartRepository) cartRepository).clear();
    }

    @Test
    void shouldAddItemToCart() throws Exception {
        String cartId = "cart-1";
        cartRepository.save(new Cart(new CartId(cartId), new UserId(userId)));

        mockMvc.perform(post("/carts/{cartId}/items", cartId)
                .with(user(userId))
                .with(csrf()) // CORREÇÃO: Necessário para POST
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"productId\": \"product-1\", \"quantity\": 2}"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateItemQuantity() throws Exception {
        String cartId = "cart-update";
        Cart cart = new Cart(new CartId(cartId), new UserId(userId));
        // Lógica de negócio: Carrinho não pode estar vazio
        cart.addItem(new ProductId("product-1"), 2); 
        cartRepository.save(cart);

        mockMvc.perform(put("/carts/{cartId}/items/product-1", cartId)
                .with(user(userId))
                .with(csrf()) // CORREÇÃO: Necessário para PUT
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"quantity\": 5}"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldClearCart() throws Exception {
        String cartId = "cart-clear";
        cartRepository.save(new Cart(new CartId(cartId), new UserId(userId)));

        mockMvc.perform(delete("/carts/{cartId}", cartId)
                .with(user(userId))
                .with(csrf())) // CORREÇÃO: Necessário para DELETE
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistingCart() throws Exception {
        mockMvc.perform(put("/carts/non-existent/items/product-1")
                .with(user(userId))
                .with(csrf()) // CORREÇÃO: Necessário para PUT (mesmo esperando 404)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"quantity\": 5}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRemoveItemFromCart() throws Exception {
        String cartId = "cart-remove";
        Cart cart = new Cart(new CartId(cartId), new UserId(userId));
        cart.addItem(new ProductId("product-1"), 2);
        cartRepository.save(cart);

        mockMvc.perform(delete("/carts/{cartId}/items/product-1", cartId)
                .with(user(userId))
                .with(csrf())) // CORREÇÃO: Necessário para DELETE
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnEmptyCartWhenCartExistsButHasNoItems() throws Exception {
        String cartId = "cart-noitems";
        cartRepository.save(new Cart(new CartId(cartId), new UserId(userId)));

        // GET não precisa de CSRF, mas precisa de user autenticado
        mockMvc.perform(get("/carts/{cartId}", cartId)
                .with(user(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value(cartId))
                .andExpect(jsonPath("$.items").isEmpty());
    }

    static class TestConfig {
        @Bean
        CartRepository cartRepository() { return new InMemoryCartRepository(); }
        @Bean
        AddItemToCartUseCase addItemToCartUseCase(CartRepository repo) { return new AddItemToCartUseCase(repo); }
        @Bean
        RemoveItemFromCartUseCase removeItemFromCartUseCase(CartRepository repo) { return new RemoveItemFromCartUseCase(repo); }
        @Bean
        UpdateCartItemQuantityUseCase updateCartItemQuantityUseCase(CartRepository repo) { return new UpdateCartItemQuantityUseCase(repo); }
        @Bean
        ClearCartUseCase clearCartUseCase(CartRepository repo) { return new ClearCartUseCase(repo); }
        @Bean
        GetCartUseCase getCartUseCase(CartRepository repo) { return new GetCartUseCase(repo); }
        @Bean
        ApiExceptionHandler apiExceptionHandler() { return new ApiExceptionHandler(); }
    }
}
package com.example.ecommerce.infrastructure.web.cart;

import com.example.ecommerce.application.cart.*;
import com.example.ecommerce.application.security.CurrentUser;
import com.example.ecommerce.domain.exception.CartNotFoundException;
import com.example.ecommerce.infrastructure.web.ApiExceptionHandler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CartController.class)
@Import(ApiExceptionHandler.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private AddItemToCartUseCase addItemToCartUseCase;
    @MockBean private UpdateCartItemQuantityUseCase updateCartItemQuantityUseCase;
    @MockBean private RemoveItemFromCartUseCase removeItemFromCartUseCase;
    @MockBean private ClearCartUseCase clearCartUseCase;
    @MockBean private GetCartUseCase getCartUseCase;

    @Test
    @WithMockUser(username = "user-1")
    void shouldAddItemToCart() throws Exception {
        mockMvc.perform(post("/carts/cart-1/items")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "productId": "product-1",
                                  "quantity": 2
                                }
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user-1")
    void shouldUpdateItemQuantity() throws Exception {
        mockMvc.perform(put("/carts/cart-1/items/product-1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "quantity": 5
                                }
                                """))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user-1")
    void shouldRemoveItemFromCart() throws Exception {
        mockMvc.perform(delete("/carts/cart-1/items/product-1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user-1")
    void shouldClearCart() throws Exception {
        mockMvc.perform(delete("/carts/cart-1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user-1")
    void shouldReturn404WhenUpdatingNonExistingCart() throws Exception {
        doThrow(new CartNotFoundException("cart-404"))
                .when(updateCartItemQuantityUseCase)
                .execute(
                        anyString(),
                        any(CurrentUser.class),
                        anyString(),
                        anyInt()
                );

        mockMvc.perform(put("/carts/cart-404/items/product-1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "quantity": 3
                                }
                                """))
                .andExpect(status().isNotFound());
    }
}

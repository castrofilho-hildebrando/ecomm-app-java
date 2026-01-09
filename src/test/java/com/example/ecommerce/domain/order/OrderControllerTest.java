package com.example.ecommerce.infrastructure.web.order;

import com.example.ecommerce.application.order.GetOrderUseCase;
import com.example.ecommerce.application.order.PayOrderUseCase;
import com.example.ecommerce.application.order.PlaceOrderFromCartUseCase;
import com.example.ecommerce.domain.cart.ProductId;
import com.example.ecommerce.domain.order.*;
import com.example.ecommerce.infrastructure.persistence.memory.order.InMemoryOrderRepository;
import com.example.ecommerce.infrastructure.web.ApiExceptionHandler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderController.class)
@Import(OrderControllerTest.TestConfig.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InMemoryOrderRepository orderRepository;

    @MockBean
    private PlaceOrderFromCartUseCase placeOrderFromCartUseCase;

    @Test
    @WithMockUser(username = "user-1")
    void shouldNotAllowPayingAlreadyPaidOrder() throws Exception {
        Order order = new Order(new OrderId("order-1"));
        order.addItem(new OrderItem(
                new ProductId("product-1"),
                BigDecimal.TEN,
                2
        ));
        order.markAsPaid();
        orderRepository.save(order);

        mockMvc.perform(post("/orders/order-1/pay")
                        .with(csrf()))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "user-1")
    void shouldReturnOrderById() throws Exception {
        Order order = new Order(new OrderId("order-1"));
        order.addItem(new OrderItem(
                new ProductId("product-1"),
                BigDecimal.TEN,
                2
        ));
        orderRepository.save(order);

        mockMvc.perform(get("/orders/order-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value("order-1"))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.items[0].productId").value("product-1"))
                .andExpect(jsonPath("$.items[0].quantity").value(2));
    }

    static class TestConfig {

        @Bean
        InMemoryOrderRepository orderRepository() {
            return new InMemoryOrderRepository();
        }

        @Bean
        PayOrderUseCase payOrderUseCase(OrderRepository repository) {
            return new PayOrderUseCase(repository, event -> {});
        }

        @Bean
        GetOrderUseCase getOrderUseCase(OrderRepository repository) {
            return new GetOrderUseCase(repository);
        }

        @Bean
        ApiExceptionHandler apiExceptionHandler() {
            return new ApiExceptionHandler();
        }
    }
}

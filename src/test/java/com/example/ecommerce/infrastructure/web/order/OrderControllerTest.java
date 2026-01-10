package com.example.ecommerce.infrastructure.web.order;

import com.example.ecommerce.application.order.*;
import com.example.ecommerce.domain.order.*;
import com.example.ecommerce.domain.cart.CartRepository;
import com.example.ecommerce.infrastructure.persistence.memory.cart.InMemoryCartRepository;
import com.example.ecommerce.infrastructure.persistence.memory.order.InMemoryOrderRepository;
import com.example.ecommerce.infrastructure.web.ApiExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderController.class)
@Import(OrderControllerTest.TestConfig.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldNotAllowPayingAlreadyPaidOrder() throws Exception {
        OrderId orderId = new OrderId("order-1");
        Order order = new Order(orderId, List.of(new OrderItem(new OrderProductId("p1"), 1)));
        order.markAsPaid();
        orderRepository.save(order);

        mockMvc.perform(post("/orders/{orderId}/pay", orderId.value())
                .header("Idempotency-Key", "key-123")
                .with(user("user-1"))
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnOrderById() throws Exception {
        Order order = new Order(
                new OrderId("order-1"),
                List.of(new OrderItem(new OrderProductId("product-1"), 2))
        );
        orderRepository.save(order);

        mockMvc.perform(get("/orders/order-1")
                .with(user("user-1")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("order-1"));
    }

    static class TestConfig {
        @Bean
        public OrderRepository orderRepository() { return new InMemoryOrderRepository(); }
        @Bean
        public CartRepository cartRepository() { return new InMemoryCartRepository(); }
        @Bean
        public PlaceOrderFromCartUseCase placeOrderFromCartUseCase(CartRepository cr, OrderRepository or) {
            return new PlaceOrderFromCartUseCase(cr, or, event -> {});
        }
        @Bean
        public GetOrderUseCase getOrderUseCase(OrderRepository repo) { return new GetOrderUseCase(repo); }
        @Bean
        public PayOrderUseCase payOrderUseCase(OrderRepository repo) {
            return new PayOrderUseCase(
                repo,
                new InMemoryIdempotencyRepository(),
                event -> {}
            );
        }
        @Bean
        public ApiExceptionHandler apiExceptionHandler() { return new ApiExceptionHandler(); }
    }
}
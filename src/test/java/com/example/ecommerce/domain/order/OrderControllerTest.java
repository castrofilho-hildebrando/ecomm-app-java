package com.example.ecommerce.infrastructure.web.order;

import com.example.ecommerce.application.order.PayOrderUseCase;
import com.example.ecommerce.application.order.GetOrderUseCase;
import com.example.ecommerce.domain.order.*;
import com.example.ecommerce.domain.cart.ProductId;
import com.example.ecommerce.infrastructure.persistence.memory.order.InMemoryOrderRepository;
import com.example.ecommerce.infrastructure.web.ApiExceptionHandler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderController.class)
@Import(OrderControllerTest.TestConfig.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InMemoryOrderRepository orderRepository;

    @Test
    void shouldNotAllowPayingAlreadyPaidOrder() throws Exception {
        orderRepository.clear();

        Order order = new Order(
                new OrderId("order-1"),
                List.of(new OrderItem(new ProductId("product-1"), 1))
        );

        order.pullDomainEvents(); // clear OrderCreated
        order.markAsPaid();

        orderRepository.save(order);

        mockMvc.perform(post("/orders/order-1/pay"))
                .andExpect(status().isConflict());

    }

    @Test
    void shouldReturnOrderById() throws Exception {
        orderRepository.clear();

        Order order = new Order(
                new OrderId("order-1"),
                List.of(
                        new OrderItem(new ProductId("product-1"), 2),
                        new OrderItem(new ProductId("product-2"), 1)
                )
        );

        order.pullDomainEvents();
        orderRepository.save(order);

        mockMvc.perform(get("/orders/order-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value("order-1"))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(2));
    }

    static class TestConfig {

        @Bean
        InMemoryOrderRepository orderRepository() {
            return new InMemoryOrderRepository();
        }

        @Bean
        PayOrderUseCase payOrderUseCase(
                OrderRepository repo
        ) {
            return new PayOrderUseCase(repo, event -> {});
        }

        @Bean
        GetOrderUseCase getOrderUseCase(
                OrderRepository orderRepository
        ) {
            return new GetOrderUseCase(orderRepository);
        }

        @Bean
        ApiExceptionHandler apiExceptionHandler() {
            return new ApiExceptionHandler();
        }
    }
}

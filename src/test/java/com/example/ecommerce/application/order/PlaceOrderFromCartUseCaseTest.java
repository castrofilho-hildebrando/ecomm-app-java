package com.example.ecommerce.application.order;

import com.example.ecommerce.application.product.ProductGateway;
import com.example.ecommerce.application.security.CurrentUser;
import com.example.ecommerce.application.security.FixedCurrentUser;
import com.example.ecommerce.domain.cart.*;
import com.example.ecommerce.domain.order.Order;
import com.example.ecommerce.domain.order.event.OrderCreatedEvent;
import com.example.ecommerce.domain.user.UserId;
import com.example.ecommerce.infrastructure.persistence.memory.cart.InMemoryCartRepository;
import com.example.ecommerce.infrastructure.persistence.memory.order.InMemoryOrderRepository;
import com.example.ecommerce.domain.product.ProductSnapshot;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PlaceOrderFromCartUseCaseTest {

    @Test
    void shouldPlaceOrderFromCartAndClearCart() {
        InMemoryCartRepository cartRepository = new InMemoryCartRepository();
        InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();

        ProductGateway productGateway = productId ->
                new ProductSnapshot(
                        productId.value(),
                        "Test Product",
                        BigDecimal.TEN
                );

        FakeDomainEventPublisher publisher = new FakeDomainEventPublisher();

        PlaceOrderFromCartUseCase useCase =
                new PlaceOrderFromCartUseCase(
                        cartRepository,
                        orderRepository,
                        productGateway,
                        publisher
                );

        UserId userId = new UserId("user-1");
        CurrentUser currentUser = new FixedCurrentUser("user-1");

        Cart cart = new Cart(new CartId("cart-1"), userId);
        cart.addItem(new ProductId("product-1"), 2);
        cart.addItem(new ProductId("product-2"), 1);

        cartRepository.save(cart);

        useCase.execute("cart-1", currentUser);

        assertEquals(1, publisher.count());
        assertTrue(publisher.first() instanceof OrderCreatedEvent);

        Order order = orderRepository
                .findAll()
                .get(0);

        assertEquals(2, order.getItems().size());

        Cart persistedCart = cartRepository
                .findById(new CartId("cart-1"))
                .orElseThrow();

        assertTrue(persistedCart.isEmpty());
    }
}

package com.example.ecommerce.application.order;

import com.example.ecommerce.application.security.CurrentUser;
import com.example.ecommerce.application.security.FixedCurrentUser;
import com.example.ecommerce.domain.cart.*;
import com.example.ecommerce.domain.order.Order;
import com.example.ecommerce.domain.user.UserId;
import com.example.ecommerce.domain.order.OrderRepository;
import com.example.ecommerce.domain.order.event.OrderCreatedEvent;
import com.example.ecommerce.infrastructure.persistence.memory.cart.InMemoryCartRepository;
import com.example.ecommerce.infrastructure.persistence.memory.order.InMemoryOrderRepository;
import com.example.ecommerce.domain.exception.EmptyCartException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlaceOrderFromCartUseCaseTest {

    private CartRepository cartRepository;
    private OrderRepository orderRepository;
    private PlaceOrderFromCartUseCase useCase;

    @BeforeEach
    void setUp() {
        this.cartRepository = new InMemoryCartRepository();
        this.orderRepository = new InMemoryOrderRepository();

        this.useCase = new PlaceOrderFromCartUseCase(
            cartRepository,
            orderRepository,
            event -> {}
        );
    }

    @Test
    void shouldPlaceOrderFromCartAndClearCart() {
        InMemoryCartRepository cartRepository = new InMemoryCartRepository();
        InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();

        orderRepository.clear();

        UserId userId = new UserId("user-1");
        Cart cart = new Cart(new CartId("cart-1"), userId);
        cart.addItem(new ProductId("product-1"), 2);
        cart.addItem(new ProductId("product-2"), 1);

        cartRepository.save(cart);

        FakeDomainEventPublisher publisher = new FakeDomainEventPublisher();

        PlaceOrderFromCartUseCase useCase = new PlaceOrderFromCartUseCase(
                cartRepository,
                orderRepository,
                publisher
        );

        CurrentUser currentUser = new FixedCurrentUser(userId.value());

        useCase.execute("cart-1", currentUser);

        assertEquals(1, publisher.count());
        assertTrue(publisher.first() instanceof OrderCreatedEvent);

        OrderCreatedEvent event = (OrderCreatedEvent) publisher.first();

        Order order = orderRepository
                .findById(event.orderId())
                .orElseThrow();

        assertEquals(2, order.getItems().size());

        Cart persistedCart = cartRepository
                .findById(new CartId("cart-1"))
                .orElseThrow();

        assertTrue(persistedCart.isEmpty());
    }

    @Test
    void shouldThrowWhenCartIsEmpty() {
        String cartId = "cart-empty";
        UserId userId = new UserId("user-1");

        cartRepository.save(new Cart(new CartId(cartId), userId));

        CurrentUser currentUser = new FixedCurrentUser(userId.value());

        assertThrows(EmptyCartException.class, () -> {
            useCase.execute(cartId, currentUser);
        });
    }
}
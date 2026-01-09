package com.example.ecommerce.application.order;

import com.example.ecommerce.domain.cart.Cart;
import com.example.ecommerce.domain.cart.CartId;
import com.example.ecommerce.domain.cart.ProductId;
import com.example.ecommerce.domain.exception.CartNotFoundException;
import com.example.ecommerce.domain.exception.EmptyCartException;
import com.example.ecommerce.domain.order.Order;
import com.example.ecommerce.domain.order.event.OrderCreatedEvent;
import com.example.ecommerce.application.event.DomainEventPublisher;
import com.example.ecommerce.infrastructure.event.LoggingDomainEventPublisher;
import com.example.ecommerce.infrastructure.persistence.memory.cart.InMemoryCartRepository;
import com.example.ecommerce.infrastructure.persistence.memory.order.InMemoryOrderRepository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaceOrderFromCartUseCaseTest {

    @Test
    void shouldPlaceOrderFromCartAndClearCart() {
        InMemoryCartRepository cartRepository = new InMemoryCartRepository();
        InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();

        orderRepository.clear();

        Cart cart = new Cart(new CartId("cart-1"));
        cart.addItem(new ProductId("product-1"), 2);
        cart.addItem(new ProductId("product-2"), 1);

        cartRepository.save(cart);

        FakeDomainEventPublisher publisher = new FakeDomainEventPublisher();

        PlaceOrderFromCartUseCase useCase =
                new PlaceOrderFromCartUseCase(
                        cartRepository,
                        orderRepository,
                        publisher
                );

        useCase.execute("cart-1");

        assertEquals(1, publisher.count());
        assertTrue(publisher.first() instanceof OrderCreatedEvent);

        OrderCreatedEvent event =
                (OrderCreatedEvent) publisher.first();

        Order order = orderRepository
                .findById(event.orderId())
                .orElseThrow();

        assertEquals(2, order.getItems().size());

        Cart persistedCart = cartRepository
                .findById(new CartId("cart-1"))
                .orElseThrow();

        assertTrue(persistedCart.isEmpty());
    }


    @Test void shouldThrowWhenCartIsEmpty() {
            InMemoryCartRepository cartRepository = new InMemoryCartRepository();
            cartRepository.save(new Cart(new CartId("cart-empty")));
    
            PlaceOrderFromCartUseCase useCase =
                    new PlaceOrderFromCartUseCase(
                            cartRepository,
                            new InMemoryOrderRepository(),
                            event -> {} // publisher fake
            );

            assertThrows( EmptyCartException.class, () -> useCase.execute("cart-empty") );
    }
}

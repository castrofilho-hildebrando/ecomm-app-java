package com.example.ecommerce.application.order;

import com.example.ecommerce.application.event.DomainEventPublisher;
import com.example.ecommerce.domain.cart.*;
import com.example.ecommerce.domain.order.*;
import com.example.ecommerce.domain.exception.CartNotFoundException;
import com.example.ecommerce.domain.exception.EmptyCartException;
import com.example.ecommerce.application.security.CurrentUser;
import com.example.ecommerce.infrastructure.mapper.OrderMapper;

public class PlaceOrderFromCartUseCase {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final DomainEventPublisher eventPublisher;

    public PlaceOrderFromCartUseCase(
            CartRepository cartRepository,
            OrderRepository orderRepository,
            DomainEventPublisher eventPublisher
    ) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    // CHANGED: Return OrderView instead of void
    public OrderView execute(String cartId, CurrentUser currentUser) {
        Cart cart = cartRepository.findById(new CartId(cartId))
                .orElseThrow(() -> new CartNotFoundException(cartId));

        if (cart.isEmpty()) {
            throw new EmptyCartException(cartId);
        }

        CartOwnershipPolicy.assertOwner(cart, currentUser.id());

        // Create Order from Cart items
        OrderId orderId = new OrderId("ORD-" + cartId); // Or use a proper ID generator
        Order order = new Order(
                orderId,
                cart.getItems().stream()
                        .map(item -> new OrderItem(item.getProductId(), item.getQuantity()))
                        .toList()
        );

        orderRepository.save(order);

        // Publish events (OrderCreatedEvent, etc.)
        order.pullDomainEvents().forEach(eventPublisher::publish);

        // Clear the cart after successful order placement
        cart.clear();
        cartRepository.save(cart);

        // FIX: Return the mapped view
        return OrderMapper.toView(order);
    }
}
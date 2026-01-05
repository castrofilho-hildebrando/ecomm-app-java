package com.example.ecommerce.application.order;

import com.example.ecommerce.domain.cart.Cart;
import com.example.ecommerce.domain.cart.CartId;
import com.example.ecommerce.domain.cart.CartRepository;
import com.example.ecommerce.domain.user.UserId;
import com.example.ecommerce.application.security.CurrentUser;
import com.example.ecommerce.domain.exception.CartNotFoundException;
import com.example.ecommerce.domain.exception.EmptyCartException;
import com.example.ecommerce.domain.order.Order;
import com.example.ecommerce.domain.order.OrderId;
import com.example.ecommerce.domain.order.OrderItem;
import com.example.ecommerce.domain.order.OrderRepository;
import com.example.ecommerce.application.event.EventConsumer;
import com.example.ecommerce.domain.event.DomainEvent;
import com.example.ecommerce.application.event.DomainEventPublisher;

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

    private Order createOrderFromCart(Cart cart) {
        return new Order(
                OrderId.newId(),
                cart.getItems().stream()
                        .map(item -> new OrderItem(
                                item.getProductId(),
                                item.getQuantity()
                        ))
                        .toList()
        );
    }

    public void execute(String cartId, String userId) {
        UserId uid = currentUser.id();

        Cart cart = cartRepository.findById(new CartId(cartId))
                .orElseThrow(() -> new CartNotFoundException(cartId));

        if (cart.isEmpty()) {
            throw new EmptyCartException(cartId);
        }

        CartOwnershipPolicy.assertOwner(cart, uid);

        Order order = createOrderFromCart(cart);
        orderRepository.save(order);

        EventConsumer consumer = new EventConsumer(eventPublisher);

        for (DomainEvent event : order.pullDomainEvents()) {
            consumer.accept(event);
        }

        for (DomainEvent event : aggregate.pullDomainEvents()) {
            publisher.publish(event);
        }

        cart.clear();
        cartRepository.save(cart);
    }
}

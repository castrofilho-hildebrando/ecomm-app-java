package com.example.ecommerce.application.order;

import com.example.ecommerce.application.event.DomainEventPublisher;
import com.example.ecommerce.application.security.CurrentUser;
import com.example.ecommerce.domain.cart.Cart;
import com.example.ecommerce.domain.cart.CartId;
import com.example.ecommerce.domain.cart.CartOwnershipPolicy;
import com.example.ecommerce.domain.cart.CartRepository;
import com.example.ecommerce.domain.exception.CartNotFoundException;
import com.example.ecommerce.domain.exception.EmptyCartException;
import com.example.ecommerce.domain.order.Order;
import com.example.ecommerce.domain.order.OrderId;
import com.example.ecommerce.domain.order.OrderItem;
import com.example.ecommerce.domain.order.OrderProductId;
import com.example.ecommerce.domain.order.OrderRepository;
import org.springframework.transaction.annotation.Transactional;

public class PlaceOrderFromCartUseCase {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final DomainEventPublisher publisher;

    public PlaceOrderFromCartUseCase(
            CartRepository cartRepository,
            OrderRepository orderRepository,
            DomainEventPublisher publisher
    ) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.publisher = publisher;
    }

    @Transactional
    public OrderView execute(String cartId, CurrentUser currentUser) {
        Cart cart = cartRepository.findById(new CartId(cartId))
                .orElseThrow(() -> new CartNotFoundException(cartId));

        if (cart.isEmpty()) {
            throw new EmptyCartException(cartId);
        }

        CartOwnershipPolicy.assertOwner(cart, currentUser.id());

        Order order = new Order(
                OrderId.newId(),
                cart.getItems().values().stream()
                        .map(item -> new OrderItem(
                                new OrderProductId(item.getProductId().value()),
                                item.getQuantity()
                        ))
                        .toList()
        );

        orderRepository.save(order);
        order.pullDomainEvents().forEach(publisher::publish);

        cart.clear();
        cartRepository.save(cart);

        return OrderView.from(order);
    }
}

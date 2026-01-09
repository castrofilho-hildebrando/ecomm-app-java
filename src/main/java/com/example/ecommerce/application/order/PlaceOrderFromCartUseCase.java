package com.example.ecommerce.application.order;

import com.example.ecommerce.application.cart.CartOwnershipPolicy;
import com.example.ecommerce.application.event.DomainEventPublisher;
import com.example.ecommerce.application.product.ProductGateway;
import com.example.ecommerce.domain.product.ProductSnapshot;
import com.example.ecommerce.application.security.CurrentUser;
import com.example.ecommerce.domain.cart.Cart;
import com.example.ecommerce.domain.cart.CartId;
import com.example.ecommerce.domain.cart.CartRepository;
import com.example.ecommerce.domain.exception.CartNotFoundException;
import com.example.ecommerce.domain.order.Order;
import com.example.ecommerce.domain.order.OrderId;
import com.example.ecommerce.domain.order.OrderItem;
import com.example.ecommerce.domain.order.OrderRepository;
import com.example.ecommerce.domain.user.UserId;

public class PlaceOrderFromCartUseCase {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ProductGateway productGateway;
    private final DomainEventPublisher eventPublisher;

    public PlaceOrderFromCartUseCase(
            CartRepository cartRepository,
            OrderRepository orderRepository,
            ProductGateway productGateway,
            DomainEventPublisher eventPublisher
    ) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.productGateway = productGateway;
        this.eventPublisher = eventPublisher;
    }

    public void execute(String cartId, CurrentUser currentUser) {
        UserId userId = currentUser.id();

        Cart cart = cartRepository.findById(new CartId(cartId))
                .orElseThrow(() -> new CartNotFoundException(cartId));

        CartOwnershipPolicy.assertOwner(cart, userId);
        cart.validateNotEmpty();

        Order order = new Order(OrderId.newId());

        cart.getItems().forEach(cartItem -> {
            var snapshot = productGateway.getProduct(cartItem.getProductId());
            order.addItem(new OrderItem(
                    cartItem.getProductId(),
                    snapshot.price(),
                    cartItem.getQuantity()
            ));
        });

        orderRepository.save(order);
        order.pullDomainEvents().forEach(eventPublisher::publish);

        cart.clear();
        cartRepository.save(cart);
    }
}

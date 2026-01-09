package com.example.ecommerce.application.order;

import com.example.ecommerce.domain.exception.OrderNotFoundException;
import com.example.ecommerce.domain.order.Order;
import com.example.ecommerce.domain.order.OrderId;
import com.example.ecommerce.domain.order.OrderRepository;
import com.example.ecommerce.domain.user.UserId;

public class GetOrderUseCase {

    private final OrderRepository orderRepository;

    public GetOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderView execute(String orderId,  String userId) {
        Order order = orderRepository
                .findById(new OrderId(orderId))
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        UserId uid = new UserId(userId);

        Cart cart = cartRepository.findById(new CartId(cartId))
                .orElseThrow(() -> new CartNotFoundException(cartId));

        CartOwnershipPolicy.assertOwner(cart, uid);

        if (cart.isEmpty()) {
            throw new EmptyCartException(cartId);
        }

        return new OrderView(
                order.getId().value(),
                order.getStatus().name(),
                order.getItems().stream()
                        .map(item -> new OrderItemView(
                                item.getProductId().value(),
                                item.getQuantity()
                        ))
                        .toList()
        );
    }
}

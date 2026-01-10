package com.example.ecommerce.infrastructure.web.order;

import com.example.ecommerce.application.order.GetOrderUseCase;
import com.example.ecommerce.application.order.PayOrderUseCase;
import com.example.ecommerce.application.order.PlaceOrderFromCartUseCase;
import com.example.ecommerce.application.security.CurrentUser;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final PlaceOrderFromCartUseCase placeOrderFromCart;
    private final GetOrderUseCase getOrder;
    private final PayOrderUseCase payOrder;

    public OrderController(
            PlaceOrderFromCartUseCase placeOrderFromCart,
            GetOrderUseCase getOrder,
            PayOrderUseCase payOrder
    ) {
        this.placeOrderFromCart = placeOrderFromCart;
        this.getOrder = getOrder;
        this.payOrder = payOrder;
    }

    @PostMapping("/from-cart/{cartId}")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeFromCart(
            @PathVariable String cartId,
            CurrentUser currentUser
    ) {
        return OrderResponse.from(
                placeOrderFromCart.execute(cartId, currentUser)
        );
    }

    @GetMapping("/{orderId}")
    public OrderResponse get(
            @PathVariable String orderId,
            CurrentUser currentUser
    ) {
        return OrderResponse.from(
                getOrder.execute(orderId, currentUser)
        );
    }

    @PostMapping("/{orderId}/pay")
    public OrderResponse pay(
            @PathVariable String orderId,
            @RequestHeader("Idempotency-Key") @NotBlank String idempotencyKey,
            CurrentUser currentUser
    ) {
        return OrderResponse.from(
                payOrder.execute(orderId, currentUser, idempotencyKey)
        );
    }
}

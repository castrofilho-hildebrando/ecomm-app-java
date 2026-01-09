package com.example.ecommerce.infrastructure.web.order;

import com.example.ecommerce.application.order.PlaceOrderFromCartUseCase;
import com.example.ecommerce.application.order.PayOrderUseCase;
import com.example.ecommerce.application.order.GetOrderUseCase;
import com.example.ecommerce.application.security.CurrentUser;
import com.example.ecommerce.infrastructure.security.SecurityContextCurrentUser;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final PlaceOrderFromCartUseCase placeOrderFromCartUseCase;
    private final PayOrderUseCase payOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;

    public OrderController(
            PlaceOrderFromCartUseCase placeOrderFromCartUseCase,
            PayOrderUseCase payOrderUseCase,
            GetOrderUseCase getOrderUseCase
    ) {
        this.placeOrderFromCartUseCase = placeOrderFromCartUseCase;
        this.payOrderUseCase = payOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
    }

    @PostMapping("/from-cart/{cartId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void placeOrder(@PathVariable String cartId) {
        CurrentUser currentUser =
                new SecurityContextCurrentUser(
                        SecurityContextHolder.getContext().getAuthentication()
                );

        placeOrderFromCartUseCase.execute(cartId, currentUser);
    }

    @PostMapping("/{orderId}/pay")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void pay(@PathVariable String orderId) {
        payOrderUseCase.execute(orderId);
    }

    @GetMapping("/{orderId}")
    public OrderView getById(@PathVariable String orderId) {
        return getOrderUseCase.execute(orderId);
    }
}

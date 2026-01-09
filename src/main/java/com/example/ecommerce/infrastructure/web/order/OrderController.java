package com.example.ecommerce.infrastructure.web.order;

import com.example.ecommerce.application.order.*; // Isso importa o OrderView correto
import com.example.ecommerce.application.security.CurrentUser;
import com.example.ecommerce.infrastructure.security.SecurityContextCurrentUser;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final PayOrderUseCase payOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final PlaceOrderFromCartUseCase placeOrderFromCartUseCase;

    public OrderController(
            PayOrderUseCase payOrderUseCase,
            GetOrderUseCase getOrderUseCase,
            PlaceOrderFromCartUseCase placeOrderFromCartUseCase
    ) {
        this.payOrderUseCase = payOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
        this.placeOrderFromCartUseCase = placeOrderFromCartUseCase;
    }

    @PostMapping("/from-cart/{cartId}")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderView placeFromCart(@PathVariable String cartId, Authentication authentication) {
        CurrentUser currentUser = new SecurityContextCurrentUser(authentication);
        return placeOrderFromCartUseCase.execute(cartId, currentUser);
    }

    @GetMapping("/{orderId}")
    public OrderView get(@PathVariable String orderId, Authentication authentication) {
        CurrentUser currentUser = new SecurityContextCurrentUser(authentication);
        return getOrderUseCase.execute(orderId, currentUser);
    }

    @PostMapping("/{orderId}/pay")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void pay(@PathVariable String orderId) {
        payOrderUseCase.execute(orderId);
    }
}
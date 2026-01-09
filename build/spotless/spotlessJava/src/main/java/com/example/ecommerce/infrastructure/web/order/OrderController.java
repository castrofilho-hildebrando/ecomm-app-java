package com.example.ecommerce.infrastructure.web.order;

import com.example.ecommerce.application.order.PayOrderUseCase;
import com.example.ecommerce.application.order.PlaceOrderFromCartUseCase;
import com.example.ecommerce.application.order.GetOrderUseCase;
import com.example.ecommerce.application.order.OrderView;
import com.example.ecommerce.application.security.CurrentUser;
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
    public OrderView placeFromCart(@PathVariable String cartId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CurrentUser currentUser = new SecurityContextCurrentUser(auth);
        return placeOrderFromCartUseCase.execute(cartId, currentUser);
    }

    @PostMapping("/{orderId}/pay")
    public OrderView pay(@PathVariable String orderId) {
        CurrentUser currentUser =
                new SecurityContextCurrentUser(
                    SecurityContextHolder.getContext().getAuthentication()
        );

payOrderUseCase.execute(orderId, currentUser);
    }

    @GetMapping("/{orderId}")
    public OrderView get(@PathVariable String orderId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CurrentUser currentUser = new SecurityContextCurrentUser(auth);
        return getOrderUseCase.execute(orderId);
    }
}

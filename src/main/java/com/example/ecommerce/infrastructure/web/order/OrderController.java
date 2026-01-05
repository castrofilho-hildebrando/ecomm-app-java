package com.example.ecommerce.infrastructure.web.order;

import com.example.ecommerce.application.order.PayOrderUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.ecommerce.application.order.GetOrderUseCase;
import com.example.ecommerce.application.order.OrderView;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final PayOrderUseCase payOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;

    public OrderController(PayOrderUseCase payOrderUseCase, GetOrderUseCase getOrderUseCase) {
        this.payOrderUseCase = payOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
    }

    @PostMapping("/from-cart/{cartId}")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderView placeFromCart(
        @PathVariable String cartId
    ) {
        CurrentUser currentUser = new FixedCurrentUser("user-1");
        return placeOrderFromCartUseCase.execute(cartId, currentUser);
    }

    @PostMapping("/{orderId}/pay")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public OrderView pay(@PathVariable String orderId) {
        payOrderUseCase.execute(orderId);
    }

    @GetMapping("/{orderId}")
    public OrderView get(@PathVariable String orderId) {
        return getOrderUseCase.execute(orderId);
    }
}

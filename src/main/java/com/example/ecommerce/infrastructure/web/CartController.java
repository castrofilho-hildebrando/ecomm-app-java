package com.example.ecommerce.infrastructure.web.cart;

import com.example.ecommerce.application.cart.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final AddItemToCartUseCase addItemToCartUseCase;
    private final RemoveItemFromCartUseCase removeItemFromCartUseCase;
    private final UpdateCartItemQuantityUseCase updateCartItemQuantityUseCase;
    private final ClearCartUseCase clearCartUseCase;
    private final GetCartUseCase getCartUseCase;

    public CartController(
            AddItemToCartUseCase addItemToCartUseCase,
            RemoveItemFromCartUseCase removeItemFromCartUseCase,
            UpdateCartItemQuantityUseCase updateCartItemQuantityUseCase,
            ClearCartUseCase clearCartUseCase,
            GetCartUseCase getCartUseCase
    ) {
        this.addItemToCartUseCase = addItemToCartUseCase;
        this.removeItemFromCartUseCase = removeItemFromCartUseCase;
        this.updateCartItemQuantityUseCase = updateCartItemQuantityUseCase;
        this.clearCartUseCase = clearCartUseCase;
        this.getCartUseCase = getCartUseCase;
    }

    @PostMapping("/{cartId}/items")
    public void addItem(
            @PathVariable String cartId,
            @RequestBody AddItemRequest request
    ) {
        addItemToCartUseCase.execute(
                cartId,
                request.productId(),
                request.quantity()
        );
    }

    @PutMapping("/{cartId}/items/{productId}")
    public void updateItemQuantity(
            @PathVariable String cartId,
            @PathVariable String productId,
            @RequestBody UpdateItemQuantityRequest request
    ) {
        updateCartItemQuantityUseCase.execute(
                cartId,
                productId,
                request.quantity()
        );
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public void removeItem(
            @PathVariable String cartId,
            @PathVariable String productId
    ) {
        removeItemFromCartUseCase.execute(cartId, productId);
    }

    @DeleteMapping("/{cartId}")
    public void clearCart(@PathVariable String cartId) {
        clearCartUseCase.execute(cartId);
    }

    @GetMapping("/{cartId}")
    public CartResponse getCart(@PathVariable String cartId) {
        CartView cartView = getCartUseCase.execute(cartId);

        List<CartItemResponse> items = cartView.items().stream()
                .map(item -> new CartItemResponse(
                        item.productId(),
                        item.quantity()
                ))
                .toList();

        return new CartResponse(cartView.cartId(), items);
    }
}

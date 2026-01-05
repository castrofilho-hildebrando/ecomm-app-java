package com.example.ecommerce.infrastructure.web.cart;

import com.example.ecommerce.application.cart.*;
import com.example.ecommerce.application.security.CurrentUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

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
    public CartResponse addItem(
            @PathVariable String cartId,
            @PathVariable String userId,
            @RequestBody AddItemRequest request
    ) {
        CurrentUser currentUser = new FixedCurrentUser("user-1");
        CartView view = addItemToCartUseCase.execute(
                cartId,
                currentUser,
                request.userId,
                request.productId(),
                request.quantity()
        );

        return CartResponse.from(view);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public CartResponse updateItemQuantity(
            @PathVariable String cartId,
            @PathVariable String productId,
            @RequestBody UpdateItemQuantityRequest request
    ) {
        CurrentUser currentUser = new FixedCurrentUser("user-1");
        CartView view = updateCartItemQuantityUseCase.execute(
                cartId,
                currentUser,
                productId,
                request.quantity()
        );

        return CartResponse.from(view);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public CartResponse removeItem(
            @PathVariable String cartId,
            @PathVariable String productId
    ) {
        CurrentUser currentUser = new FixedCurrentUser("user-1");
        CartView view = removeItemFromCartUseCase.execute(
                cartId,
                currentUser,
                productId
        );
        return CartResponse.from(view);
    }

    @DeleteMapping("/{cartId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@PathVariable String cartId) {
        CurrentUser currentUser = new FixedCurrentUser("user-1");
        clearCartUseCase.execute(cartId, currentUser);
    }

    @GetMapping("/{cartId}")
    public CartResponse getCart(@PathVariable String cartId) {
        CurrentUser currentUser = new FixedCurrentUser("user-1");
        CartView cartView = getCartUseCase.execute(cartId, currentUser);

        List<CartItemResponse> items = cartView.items().stream()
                .map(item -> new CartItemResponse(
                        item.productId(),
                        item.quantity()
                ))
                .toList();

        return new CartResponse(cartView.cartId(), items);
    }
}

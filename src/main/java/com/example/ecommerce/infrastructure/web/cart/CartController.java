package com.example.ecommerce.infrastructure.web.cart;

import com.example.ecommerce.application.cart.*;
import com.example.ecommerce.application.security.CurrentUser;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final AddItemToCartUseCase addItem;
    private final UpdateCartItemQuantityUseCase updateItem;
    private final RemoveItemFromCartUseCase removeItem;
    private final ClearCartUseCase clearCart;
    private final GetCartUseCase getCart;

    public CartController(
            AddItemToCartUseCase addItem,
            UpdateCartItemQuantityUseCase updateItem,
            RemoveItemFromCartUseCase removeItem,
            ClearCartUseCase clearCart,
            GetCartUseCase getCart
    ) {
        this.addItem = addItem;
        this.updateItem = updateItem;
        this.removeItem = removeItem;
        this.clearCart = clearCart;
        this.getCart = getCart;
    }

    @GetMapping("/{cartId}")
    public CartResponse get(
            @PathVariable String cartId,
            CurrentUser currentUser
    ) {
        return CartResponse.from(
                getCart.execute(cartId, currentUser)
        );
    }

    @PostMapping("/{cartId}/items")
    public CartResponse add(
        @PathVariable String cartId,
        @Valid @RequestBody AddItemRequest request,
        CurrentUser currentUser
    ) {
        return CartResponse.from(
                addItem.execute(
                        cartId,
                        currentUser,
                        request.productId(),
                        request.quantity()
                )
        );
    }

    @PutMapping("/{cartId}/items/{productId}")
    public CartResponse update(
            @PathVariable String cartId,
            @PathVariable String productId,
            @Valid @RequestBody UpdateItemQuantityRequest request,
            CurrentUser currentUser
    ) {
        return CartResponse.from(
                updateItem.execute(
                        cartId,
                        currentUser,
                        productId,
                        request.quantity()
                )
        );
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public CartResponse removeItemFromCart(
            @PathVariable String cartId,
            @PathVariable String productId,
            CurrentUser currentUser
    ) {
        return CartResponse.from(
                removeItem.execute(cartId, currentUser, productId)
        );
    }

    @DeleteMapping("/{cartId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clear(
            @PathVariable String cartId,
            CurrentUser currentUser
    ) {
        clearCart.execute(cartId, currentUser);
    }
}
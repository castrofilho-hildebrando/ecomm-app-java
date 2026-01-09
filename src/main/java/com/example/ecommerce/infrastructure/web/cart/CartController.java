package com.example.ecommerce.infrastructure.web.cart;

import com.example.ecommerce.application.cart.*;
import com.example.ecommerce.application.security.CurrentUser;
import com.example.ecommerce.infrastructure.security.SecurityContextCurrentUser; // Added
import org.springframework.security.core.Authentication; // Added
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
            Authentication authentication, // FIX: Replaced 'Spring userId' with valid Authentication
            @RequestBody AddItemRequest request
    ) {
        // Use the security context to identify the user instead of hardcoding "user-1"
        CurrentUser currentUser = new SecurityContextCurrentUser(authentication);

        CartView view = addItemToCartUseCase.execute(
                cartId,
                currentUser,
                request.productId(),
                request.quantity()
        );
        return CartResponse.from(view);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public CartResponse updateItemQuantity(
            @PathVariable String cartId,
            @PathVariable String productId,
            Authentication authentication, // FIX: Injected security context
            @RequestBody UpdateItemQuantityRequest request
    ) {
        CurrentUser currentUser = new SecurityContextCurrentUser(authentication);
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
            @PathVariable String productId,
            Authentication authentication // FIX: Injected security context
    ) {
        CurrentUser currentUser = new SecurityContextCurrentUser(authentication);
        CartView view = removeItemFromCartUseCase.execute(
                cartId,
                currentUser,
                productId
        );
        return CartResponse.from(view);
    }

    @DeleteMapping("/{cartId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@PathVariable String cartId, Authentication authentication) {
        CurrentUser currentUser = new SecurityContextCurrentUser(authentication);
        clearCartUseCase.execute(cartId, currentUser);
    }

    @GetMapping("/{cartId}")
    public CartResponse getCart(@PathVariable String cartId, Authentication authentication) {
        CurrentUser currentUser = new SecurityContextCurrentUser(authentication);
        CartView cartView = getCartUseCase.execute(cartId, currentUser);

        return CartResponse.from(cartView);
    }
}
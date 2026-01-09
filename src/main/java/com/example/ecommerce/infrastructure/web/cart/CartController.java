package com.example.ecommerce.infrastructure.web.cart;

import com.example.ecommerce.application.cart.AddItemToCartUseCase;
import com.example.ecommerce.application.cart.UpdateCartItemQuantityUseCase;
import com.example.ecommerce.application.cart.RemoveItemFromCartUseCase;
import com.example.ecommerce.application.cart.ClearCartUseCase;
import com.example.ecommerce.application.cart.GetCartUseCase;
import com.example.ecommerce.application.cart.CartView;
import com.example.ecommerce.application.security.CurrentUser;
import com.example.ecommerce.infrastructure.security.SecurityContextCurrentUser;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final AddItemToCartUseCase addItemToCartUseCase;
    private final UpdateCartItemQuantityUseCase updateCartItemQuantityUseCase;
    private final RemoveItemFromCartUseCase removeItemFromCartUseCase;
    private final ClearCartUseCase clearCartUseCase;
    private final GetCartUseCase getCartUseCase;

    public CartController(
            AddItemToCartUseCase addItemToCartUseCase,
            UpdateCartItemQuantityUseCase updateCartItemQuantityUseCase,
            RemoveItemFromCartUseCase removeItemFromCartUseCase,
            ClearCartUseCase clearCartUseCase,
            GetCartUseCase getCartUseCase
    ) {
        this.addItemToCartUseCase = addItemToCartUseCase;
        this.updateCartItemQuantityUseCase = updateCartItemQuantityUseCase;
        this.removeItemFromCartUseCase = removeItemFromCartUseCase;
        this.clearCartUseCase = clearCartUseCase;
        this.getCartUseCase = getCartUseCase;
    }

    @PostMapping("/{cartId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public void addItem(
            @PathVariable String cartId,
            @RequestBody AddItemRequest request
    ) {
        CurrentUser currentUser = currentUser();
        addItemToCartUseCase.execute(
                cartId,
                currentUser,
                request.productId(),
                request.quantity()
        );
    }

    @PutMapping("/{cartId}/items/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateItemQuantity(
            @PathVariable String cartId,
            @PathVariable String productId,
            @RequestBody UpdateItemQuantityRequest request
    ) {
        CurrentUser currentUser = currentUser();
        updateCartItemQuantityUseCase.execute(
                cartId,
                currentUser,
                productId,
                request.quantity()
        );
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(
            @PathVariable String cartId,
            @PathVariable String productId
    ) {
        CurrentUser currentUser = currentUser();
        removeItemFromCartUseCase.execute(
                cartId,
                currentUser,
                productId
        );
    }

    @DeleteMapping("/{cartId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@PathVariable String cartId) {
        CurrentUser currentUser = currentUser();
        clearCartUseCase.execute(cartId, currentUser);
    }

    @GetMapping("/{cartId}")
    public CartView getCart(@PathVariable String cartId) {
        CurrentUser currentUser = currentUser();
        return getCartUseCase.execute(cartId, currentUser);
    }

    private CurrentUser currentUser() {
        return new SecurityContextCurrentUser(
                SecurityContextHolder.getContext().getAuthentication()
        );
    }
}

package com.example.ecommerce.domain.cart;

import com.example.ecommerce.domain.exception.CartItemNotFoundException;
import com.example.ecommerce.domain.exception.InvalidQuantityException;
import com.example.ecommerce.domain.user.UserId;
import org.springframework.data.annotation.Version;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Document(collection = "carts")
public class Cart {

    @Id
    private String id;

    private String ownerId;

    private Map<String, Integer> items = new HashMap<>();

    @Version
    private Long version;

    protected Cart() {
        // Required by Spring Data
    }

    public Cart(CartId id, UserId ownerId) {
        this.id = Objects.requireNonNull(id).value();
        this.ownerId = Objects.requireNonNull(ownerId).value();
    }

    public CartId getId() {
        return new CartId(id);
    }

    public UserId getOwnerId() {
        return new UserId(ownerId);
    }

    public Map<ProductId, CartItem> getItems() {
        Map<ProductId, CartItem> result = new HashMap<>();
        items.forEach((productId, quantity) ->
                result.put(
                        new ProductId(productId),
                        new CartItem(new ProductId(productId), quantity)
                )
        );
        return result;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void addItem(ProductId productId, int quantity) {
        if (quantity <= 0) {
            throw new InvalidQuantityException(quantity);
        }
        items.merge(productId.value(), quantity, Integer::sum);
    }

    public void updateItemQuantity(ProductId productId, int quantity) {
        if (!items.containsKey(productId.value())) {
            throw new CartItemNotFoundException(productId.value());
        }
        if (quantity <= 0) {
            items.remove(productId.value());
        } else {
            items.put(productId.value(), quantity);
        }
    }

    public void removeItem(ProductId productId) {
        if (!items.containsKey(productId.value())) {
            throw new CartItemNotFoundException(productId.value());
        }
        items.remove(productId.value());
    }

    public void clear() {
        items.clear();
    }
}

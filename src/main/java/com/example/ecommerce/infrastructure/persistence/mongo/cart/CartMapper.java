package com.example.ecommerce.infrastructure.persistence.mongo.cart;

import com.example.ecommerce.domain.cart.Cart;
import com.example.ecommerce.domain.cart.CartId;
import com.example.ecommerce.domain.cart.CartItem;
import com.example.ecommerce.domain.cart.ProductId;
import com.example.ecommerce.domain.user.UserId;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class CartMapper {

    public static Cart toDomain(CartDocument doc) {
        List<CartItem> items = doc.getItems().entrySet().stream()
                .map(e -> new CartItem(new ProductId(e.getKey()), e.getValue()))
                .toList();

        return Cart.rehydrate(
                new CartId(doc.getId()),
                new UserId(doc.getOwnerId()),
                items
        );
    }

    public static CartDocument toDocument(Cart cart) {
        CartDocument doc = new CartDocument();
        doc.setId(cart.getId().value());
        doc.setOwnerId(cart.getOwnerId().value());

        Map<String, Integer> items = new HashMap<String, Integer>();
        for (CartItem item : cart.getItems()) {
            items.put(item.getProductId().value(), item.getQuantity());
        }

        doc.setItems(Map.copyOf(items));
        return doc;
    }
}

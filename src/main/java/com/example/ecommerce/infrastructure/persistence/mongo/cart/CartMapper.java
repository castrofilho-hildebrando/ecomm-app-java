package com.example.ecommerce.infrastructure.mapper;

import com.example.ecommerce.domain.cart.Cart;
import com.example.ecommerce.domain.cart.CartId;
import com.example.ecommerce.domain.cart.ProductId;
import com.example.ecommerce.domain.user.UserId;
import com.example.ecommerce.infrastructure.persistence.mongo.cart.CartDocument;

import java.util.Map;
import java.util.stream.Collectors;

public class CartMapper {

    public static Cart toDomain(CartDocument doc) {
        Map<ProductId, Integer> items = doc.getItems().entrySet().stream()
                .collect(Collectors.toMap(
                        e -> new ProductId(e.getKey()),
                        Map.Entry::getValue
                ));

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

        cart.getItems().forEach(item ->
                doc.getItems().put(
                        item.getProductId().value(),
                        item.getQuantity()
                )
        );
        return doc;
    }
}

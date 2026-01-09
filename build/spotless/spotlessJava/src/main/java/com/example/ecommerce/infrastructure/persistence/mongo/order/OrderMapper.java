package com.example.ecommerce.infrastructure.persistence.mongo.order;

import com.example.ecommerce.domain.order.Order;
import com.example.ecommerce.domain.order.OrderId;
import com.example.ecommerce.domain.order.OrderItem;
import com.example.ecommerce.domain.cart.ProductId;

import java.util.List;

public class OrderMapper {

    public static OrderDocument toDocument(Order order) {
        OrderDocument doc = new OrderDocument();
        doc.setId(order.getId().value());

        List<OrderItem> items = doc.getItems().stream()
            .map(item -> new OrderItem(
                    new ProductId(item.getProductId()),
                    item.getPrice(),
                    item.getQuantity()
            ))
            .toList();

        doc.setStatus(order.getStatus().name());
        return doc;
    }

    public static Order toDomain(OrderDocument doc) {
        List<OrderItem> items = doc.getItems().stream()
                .map(item -> new OrderItem(
                        new ProductId(item.getProductId()),
                        item.getPrice(),
                        item.getQuantity()
                ))
                .toList();

        return Order.rehydrate(
                new OrderId(doc.getId()),
                items,
                doc.getStatus()
        );
    }
}

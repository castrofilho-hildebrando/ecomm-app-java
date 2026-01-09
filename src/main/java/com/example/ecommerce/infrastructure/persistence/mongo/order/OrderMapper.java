package com.example.ecommerce.infrastructure.persistence.mongo.order;

import com.example.ecommerce.domain.cart.ProductId;
import com.example.ecommerce.domain.order.Order;
import com.example.ecommerce.domain.order.OrderId;
import com.example.ecommerce.domain.order.OrderItem;
import com.example.ecommerce.domain.order.OrderStatus;

import java.util.List;

public class OrderMapper {

    public static OrderDocument toDocument(Order order) {
        OrderDocument doc = new OrderDocument();
        doc.setId(order.getId().value());
        doc.setStatus(order.getStatus().name());

        doc.setItems(
                order.getItems().stream()
                        .map(i -> new OrderItemDocument(
                                i.getProductId().value(),
                                i.getPrice(),
                                i.getQuantity()
                        ))
                        .toList()
        );

        return doc;
    }

    public static Order toDomain(OrderDocument doc) {
        List<OrderItem> items = doc.getItems().stream()
                .map(i -> new OrderItem(
                        new ProductId(i.getProductId()),
                        i.getPrice(),
                        i.getQuantity()
                ))
                .toList();

        return Order.rehydrate(
                new OrderId(doc.getId()),
                items,
                OrderStatus.valueOf(doc.getStatus())
        );
    }
}

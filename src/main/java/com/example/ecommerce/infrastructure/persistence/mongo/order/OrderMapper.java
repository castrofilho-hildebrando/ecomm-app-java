package com.example.ecommerce.infrastructure.persistence.order.mapper;

import com.example.ecommerce.domain.order.Order;
import com.example.ecommerce.domain.order.OrderId;
import com.example.ecommerce.domain.order.OrderItem;
import com.example.ecommerce.domain.order.OrderStatus;
import com.example.ecommerce.domain.cart.ProductId;
import com.example.ecommerce.infrastructure.persistence.mongo.order.OrderDocument;
import com.example.ecommerce.infrastructure.persistence.mongo.order.OrderItemDocument;

import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderDocument toDocument(Order order) {
        OrderDocument doc = new OrderDocument();
        doc.setId(order.getId().value());

        doc.setItems(
                order.getItems().stream()
                        .map(i -> new OrderItemDocument(
                                i.getProductId().value(),
                                i.getQuantity()
                        ))
                        .collect(Collectors.toList())
        );

        doc.setStatus(order.getStatus().name());

        return doc;
    }

    public static Order toDomain(OrderDocument doc) {
        OrderStatus status = OrderStatus.valueOf(doc.getStatus());

        return Order.rehydrate(
                new OrderId(doc.getId()),
                doc.getItems().stream()
                        .map(i -> new OrderItem(
                                new ProductId(i.getProductId()),
                                i.getQuantity()
                        ))
                        .toList(),
                status
        );
    }
}

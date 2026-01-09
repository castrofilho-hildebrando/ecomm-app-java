package com.example.ecommerce.infrastructure.mapper;

import com.example.ecommerce.domain.order.*;
import com.example.ecommerce.application.order.OrderView;
import com.example.ecommerce.application.order.OrderItemView;
import com.example.ecommerce.infrastructure.persistence.mongo.order.OrderDocument;
import com.example.ecommerce.infrastructure.persistence.mongo.order.OrderItemDocument;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderView toView(Order order) {
        // 1. Ela vem daqui: extraímos os itens do objeto de domínio 'order'
        List<OrderItemView> itemViews = order.getItems().stream() 
                // 2. Transformamos cada OrderItem em um OrderItemView
                .map(item -> new OrderItemView(
                        item.getProductId().value(), 
                        item.getQuantity()
                ))
                // 3. Agrupamos tudo em uma nova lista
                .collect(Collectors.toList());

        // 4. Agora passamos essa lista para o construtor do OrderView
        return new OrderView(
                order.getId().value(),
                itemViews, // <--- Aqui ela é utilizada
                order.getStatus().name()
        );
    }

    public static OrderDocument toDocument(Order order) {
        OrderDocument doc = new OrderDocument();
        doc.setId(order.getId().value());
        
        List<OrderItemDocument> itemDocs = order.getItems().stream()
                .map(i -> new OrderItemDocument(i.getProductId().value(), i.getQuantity()))
                .collect(Collectors.toList());
                
        doc.setItems(itemDocs);
        doc.setStatus(order.getStatus().name());
        return doc;
    }

    public static Order toDomain(OrderDocument doc) {
        List<OrderItem> items = doc.getItems().stream()
                .map(i -> new OrderItem(
                        new com.example.ecommerce.domain.cart.ProductId(i.getProductId()), 
                        i.getQuantity()
                ))
                .collect(Collectors.toList());

        return Order.rehydrate(
                new OrderId(doc.getId()),
                items,
                OrderStatus.valueOf(doc.getStatus())
        );
    }
}
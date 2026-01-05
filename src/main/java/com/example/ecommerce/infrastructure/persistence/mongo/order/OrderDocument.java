package com.example.ecommerce.infrastructure.persistence.mongo.order;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "orders")
public class OrderDocument {

    @Id
    private String id;

    private String status;

    private List<OrderItemDocument> items;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderItemDocument> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDocument> items) {
        this.items = items;
    }
}

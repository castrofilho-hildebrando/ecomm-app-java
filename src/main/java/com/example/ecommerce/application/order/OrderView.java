package com.example.ecommerce.application.order;

import java.util.List;

public record OrderView(
    String id,
    List<OrderItemView> items,
    String status
) {}

package com.example.ecommerce.application.order;

import java.util.List;

public record OrderView(
        String orderId,
        String status,
        List<OrderItemView> items
) {
}

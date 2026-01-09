package com.example.ecommerce.infrastructure.web.order;

import java.util.List;

public record OrderView(
        String orderId,
        String status,
        List<OrderItemView> items
) {
}

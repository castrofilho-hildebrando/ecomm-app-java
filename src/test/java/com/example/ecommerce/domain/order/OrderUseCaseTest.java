package com.example.ecommerce.domain.order;

import com.example.ecommerce.domain.cart.ProductId;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderUseCaseTest {

    @Test
    void shouldCreateOrderWithItems() {
        OrderId orderId = new OrderId("order-1");

        Order order = new Order(
                orderId,
                List.of(
                        new OrderItem(new ProductId("product-1"), 2),
                        new OrderItem(new ProductId("product-2"), 1)
                )
        );

        assertEquals(orderId, order.getId());
        assertEquals(2, order.getItems().size());
    }

    @Test
    void shouldExposeItemsAsUnmodifiableList() {
        Order order = new Order(
                new OrderId("order-1"),
                List.of(new OrderItem(new ProductId("product-1"), 1))
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> order.getItems().add(
                        new OrderItem(new ProductId("product-2"), 1)
                )
        );
    }

    @Test
    void shouldAllowCreatingOrderWithEmptyItemList() {
        Order order = new Order(
                new OrderId("order-1"),
                List.of()
        );

        assertTrue(order.getItems().isEmpty());
    }

    @Test
    void shouldNotAllowNullOrderIdValue() {
        assertThrows(
                NullPointerException.class,
                () -> new OrderId(null)
        );
    }

    @Test
    void shouldGenerateNewOrderId() {
        OrderId id1 = OrderId.newId();
        OrderId id2 = OrderId.newId();

        assertNotNull(id1.value());
        assertNotEquals(id1.value(), id2.value());
    }

    @Test
    void shouldCreateOrderItem() {
        ProductId productId = new ProductId("product-1");

        OrderItem item = new OrderItem(productId, 2);

        assertEquals(productId, item.getProductId());
        assertEquals(2, item.getQuantity());
    }
}

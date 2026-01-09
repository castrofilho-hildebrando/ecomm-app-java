package com.example.ecommerce.application.product;

import com.example.ecommerce.domain.cart.ProductId;
import com.example.ecommerce.domain.product.ProductSnapshot;

public interface ProductGateway {
    ProductSnapshot getProduct(ProductId productId);
}

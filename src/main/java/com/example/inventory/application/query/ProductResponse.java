package com.example.inventory.application.query;

import com.example.inventory.domain.model.Product;
import java.math.BigDecimal;

public record ProductResponse(
    String id,
    String name,
    String sku,
    int quantity,
    BigDecimal price) {

  public static ProductResponse from(Product product) {
    return new ProductResponse(
        product.id().toString(),
        product.name(),
        product.sku(),
        product.quantity(),
        product.price());
  }
}

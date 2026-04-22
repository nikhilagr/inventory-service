package com.example.inventory.domain.event;

import com.example.inventory.domain.model.ProductId;
import java.math.BigDecimal;
import java.time.Instant;

public record ProductCreated(
    ProductId productId,
    String name,
    String sku,
    int quantity,
    BigDecimal price,
    Instant occurredAt) {

  public ProductCreated(ProductId productId, String name, String sku, int quantity, BigDecimal price) {
    this(productId, name, sku, quantity, price, Instant.now());
  }
}

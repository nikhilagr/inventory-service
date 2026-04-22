package com.example.inventory.domain.event;

import com.example.inventory.domain.model.ProductId;
import java.time.Instant;

public record QuantityAdjusted(
    ProductId productId,
    int previousQuantity,
    int newQuantity,
    int delta,
    Instant occurredAt) {

  public QuantityAdjusted(ProductId productId, int previousQuantity, int newQuantity, int delta) {
    this(productId, previousQuantity, newQuantity, delta, Instant.now());
  }
}

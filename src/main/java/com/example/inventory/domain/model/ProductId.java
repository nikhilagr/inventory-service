package com.example.inventory.domain.model;

import java.util.UUID;

public record ProductId(UUID value) {

  public ProductId {
    if (value == null) {
      throw new IllegalArgumentException("ProductId value must not be null");
    }
  }

  public static ProductId generate() {
    return new ProductId(UUID.randomUUID());
  }

  public static ProductId of(UUID value) {
    return new ProductId(value);
  }

  @Override
  public String toString() {
    return value.toString();
  }
}

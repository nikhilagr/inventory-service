package com.example.inventory.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "products")
public class ProductJpaEntity {

  @Id
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true, length = 100)
  private String sku;

  @Column(nullable = false)
  private int quantity;

  @Column(nullable = false, precision = 19, scale = 4)
  private BigDecimal price;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  protected ProductJpaEntity() {}

  public ProductJpaEntity(UUID id, String name, String sku, int quantity, BigDecimal price, Instant createdAt) {
    this.id = id;
    this.name = name;
    this.sku = sku;
    this.quantity = quantity;
    this.price = price;
    this.createdAt = createdAt;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getSku() {
    return sku;
  }

  public int getQuantity() {
    return quantity;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}

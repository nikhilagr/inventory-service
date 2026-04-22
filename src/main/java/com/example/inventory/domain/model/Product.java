package com.example.inventory.domain.model;

import com.example.inventory.domain.event.ProductCreated;
import com.example.inventory.domain.event.QuantityAdjusted;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Product {

  private final ProductId id;
  private final String name;
  private final String sku;
  private int quantity;
  private final BigDecimal price;

  private final List<Object> domainEvents = new ArrayList<>();

  private Product(ProductId id, String name, String sku, int quantity, BigDecimal price) {
    this.id = id;
    this.name = name;
    this.sku = sku;
    this.quantity = quantity;
    this.price = price;
  }

  public static Product create(String name, String sku, int quantity, BigDecimal price) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Product name must not be blank");
    }
    if (sku == null || sku.isBlank()) {
      throw new IllegalArgumentException("Product SKU must not be blank");
    }
    if (quantity < 0) {
      throw new IllegalArgumentException("Product quantity must not be negative");
    }
    if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Product price must be greater than zero");
    }

    var product = new Product(ProductId.generate(), name.strip(), sku.strip(), quantity, price);
    product.domainEvents.add(new ProductCreated(product.id, product.name, product.sku, quantity, price));
    return product;
  }

  public void adjustQuantity(int delta) {
    int updated = this.quantity + delta;
    if (updated < 0) {
      throw new IllegalArgumentException(
          "Quantity adjustment of " + delta + " would result in negative stock (" + updated + ")");
    }
    int previous = this.quantity;
    this.quantity = updated;
    domainEvents.add(new QuantityAdjusted(this.id, previous, this.quantity, delta));
  }

  public List<Object> pullDomainEvents() {
    var events = List.copyOf(domainEvents);
    domainEvents.clear();
    return events;
  }

  public ProductId id() {
    return id;
  }

  public String name() {
    return name;
  }

  public String sku() {
    return sku;
  }

  public int quantity() {
    return quantity;
  }

  public BigDecimal price() {
    return price;
  }

  public List<Object> domainEvents() {
    return Collections.unmodifiableList(domainEvents);
  }
}

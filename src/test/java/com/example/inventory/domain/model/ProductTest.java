package com.example.inventory.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.inventory.domain.event.ProductCreated;
import com.example.inventory.domain.event.QuantityAdjusted;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ProductTest {

  @Test
  void create_validArguments_producesAggregateWithCreatedEvent() {
    var product = Product.create("Widget", "SKU-001", 10, new BigDecimal("9.99"));

    assertNotNull(product.id());
    assertEquals("Widget", product.name());
    assertEquals("SKU-001", product.sku());
    assertEquals(10, product.quantity());
    assertEquals(new BigDecimal("9.99"), product.price());

    var events = product.domainEvents();
    assertEquals(1, events.size());
    var event = (ProductCreated) events.get(0);
    assertEquals(product.id(), event.productId());
    assertEquals("SKU-001", event.sku());
  }

  @Test
  void create_blankSku_throwsIllegalArgument() {
    assertThrows(
        IllegalArgumentException.class,
        () -> Product.create("Widget", "  ", 0, new BigDecimal("1.00")));
  }

  @Test
  void create_blankName_throwsIllegalArgument() {
    assertThrows(
        IllegalArgumentException.class,
        () -> Product.create("", "SKU-001", 0, new BigDecimal("1.00")));
  }

  @Test
  void create_zeroPricethrowsIllegalArgument() {
    assertThrows(
        IllegalArgumentException.class,
        () -> Product.create("Widget", "SKU-001", 0, BigDecimal.ZERO));
  }

  @Test
  void create_negativeQuantity_throwsIllegalArgument() {
    assertThrows(
        IllegalArgumentException.class,
        () -> Product.create("Widget", "SKU-001", -1, new BigDecimal("1.00")));
  }

  @Test
  void adjustQuantity_positiveData_increasesStock() {
    var product = Product.create("Widget", "SKU-001", 10, new BigDecimal("9.99"));
    product.pullDomainEvents(); // clear creation event

    product.adjustQuantity(5);

    assertEquals(15, product.quantity());
    var events = product.domainEvents();
    assertEquals(1, events.size());
    var event = (QuantityAdjusted) events.get(0);
    assertEquals(10, event.previousQuantity());
    assertEquals(15, event.newQuantity());
    assertEquals(5, event.delta());
  }

  @Test
  void adjustQuantity_negativeDelta_decreasesStock() {
    var product = Product.create("Widget", "SKU-001", 10, new BigDecimal("9.99"));
    product.pullDomainEvents();

    product.adjustQuantity(-3);

    assertEquals(7, product.quantity());
  }

  @Test
  void adjustQuantity_wouldGoBelowZero_throwsIllegalArgument() {
    var product = Product.create("Widget", "SKU-001", 5, new BigDecimal("9.99"));

    assertThrows(IllegalArgumentException.class, () -> product.adjustQuantity(-6));
    assertEquals(5, product.quantity()); // quantity unchanged
  }

  @Test
  void pullDomainEvents_clearsEvents() {
    var product = Product.create("Widget", "SKU-001", 10, new BigDecimal("9.99"));

    var first = product.pullDomainEvents();
    var second = product.pullDomainEvents();

    assertEquals(1, first.size());
    assertEquals(0, second.size());
  }
}

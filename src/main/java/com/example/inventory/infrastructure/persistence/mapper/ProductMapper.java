package com.example.inventory.infrastructure.persistence.mapper;

import com.example.inventory.domain.model.Product;
import com.example.inventory.domain.model.ProductId;
import com.example.inventory.infrastructure.persistence.entity.ProductJpaEntity;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

  public Product toDomain(ProductJpaEntity entity) {
    return Product.reconstitute(
        ProductId.of(entity.getId()),
        entity.getName(),
        entity.getSku(),
        entity.getQuantity(),
        entity.getPrice());
  }

  public ProductJpaEntity toJpaEntity(Product product) {
    return new ProductJpaEntity(
        product.id().value(),
        product.name(),
        product.sku(),
        product.quantity(),
        product.price(),
        Instant.now());
  }
}

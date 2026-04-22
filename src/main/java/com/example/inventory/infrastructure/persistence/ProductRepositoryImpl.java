package com.example.inventory.infrastructure.persistence;

import com.example.inventory.domain.model.Product;
import com.example.inventory.domain.model.ProductId;
import com.example.inventory.domain.repository.ProductRepository;
import com.example.inventory.infrastructure.persistence.mapper.ProductMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

  private final JpaProductRepository jpa;
  private final ProductMapper mapper;

  public ProductRepositoryImpl(JpaProductRepository jpa, ProductMapper mapper) {
    this.jpa = jpa;
    this.mapper = mapper;
  }

  @Override
  public Optional<Product> findById(ProductId id) {
    return jpa.findById(id.value()).map(mapper::toDomain);
  }

  @Override
  public Optional<Product> findBySku(String sku) {
    return jpa.findBySku(sku).map(mapper::toDomain);
  }

  @Override
  public Product save(Product product) {
    jpa.save(mapper.toJpaEntity(product));
    return product;
  }

  @Override
  public List<Product> findAll() {
    return jpa.findAll().stream()
        .map(mapper::toDomain)
        .toList();
  }

  @Override
  public boolean existsBySku(String sku) {
    return jpa.existsBySku(sku);
  }
}

package com.example.inventory.domain.repository;

import com.example.inventory.domain.model.Product;
import com.example.inventory.domain.model.ProductId;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {

  Optional<Product> findById(ProductId id);

  Optional<Product> findBySku(String sku);

  Product save(Product product);

  List<Product> findAll();

  boolean existsBySku(String sku);
}

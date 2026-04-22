package com.example.inventory.application.service;

import com.example.inventory.application.command.AdjustQuantityCommand;
import com.example.inventory.application.command.CreateProductCommand;
import com.example.inventory.application.query.ProductResponse;
import com.example.inventory.domain.model.Product;
import com.example.inventory.domain.model.ProductId;
import com.example.inventory.domain.repository.ProductRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductApplicationService {

  private static final Logger log = LoggerFactory.getLogger(ProductApplicationService.class);

  private final ProductRepository productRepository;

  public ProductApplicationService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Transactional
  public ProductResponse createProduct(CreateProductCommand cmd) {
    if (productRepository.existsBySku(cmd.sku())) {
      throw new IllegalArgumentException("A product with SKU '" + cmd.sku() + "' already exists");
    }

    var product = Product.create(cmd.name(), cmd.sku(), cmd.quantity(), cmd.price());
    productRepository.save(product);

    product.pullDomainEvents().forEach(event -> log.info("Domain event: {}", event));

    return ProductResponse.from(product);
  }

  @Transactional
  public ProductResponse adjustQuantity(AdjustQuantityCommand cmd) {
    var productId = ProductId.of(UUID.fromString(cmd.productId()));
    var product = productRepository.findById(productId)
        .orElseThrow(() -> new NoSuchElementException("Product not found: " + cmd.productId()));

    product.adjustQuantity(cmd.delta());
    productRepository.save(product);

    product.pullDomainEvents().forEach(event -> log.info("Domain event: {}", event));

    return ProductResponse.from(product);
  }

  public ProductResponse getById(String id) {
    var productId = ProductId.of(UUID.fromString(id));
    return productRepository.findById(productId)
        .map(ProductResponse::from)
        .orElseThrow(() -> new NoSuchElementException("Product not found: " + id));
  }

  public List<ProductResponse> getAll() {
    return productRepository.findAll().stream()
        .map(ProductResponse::from)
        .toList();
  }
}

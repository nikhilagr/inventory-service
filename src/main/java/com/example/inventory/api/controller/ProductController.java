package com.example.inventory.api.controller;

import com.example.inventory.application.command.AdjustQuantityCommand;
import com.example.inventory.application.command.CreateProductCommand;
import com.example.inventory.application.query.ProductResponse;
import com.example.inventory.application.service.ProductApplicationService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

  private final ProductApplicationService service;

  public ProductController(ProductApplicationService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductCommand cmd) {
    var result = service.createProduct(cmd);
    return ResponseEntity
        .created(URI.create("/api/v1/products/" + result.id()))
        .body(result);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductResponse> getById(@PathVariable String id) {
    return ResponseEntity.ok(service.getById(id));
  }

  @GetMapping
  public ResponseEntity<List<ProductResponse>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }

  @PatchMapping("/{id}/quantity")
  public ResponseEntity<ProductResponse> adjustQuantity(
      @PathVariable String id,
      @Valid @RequestBody AdjustQuantityRequest request) {
    var cmd = new AdjustQuantityCommand(id, request.delta());
    return ResponseEntity.ok(service.adjustQuantity(cmd));
  }
}

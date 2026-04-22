package com.example.inventory.infrastructure.persistence;

import com.example.inventory.infrastructure.persistence.entity.ProductJpaEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface JpaProductRepository extends JpaRepository<ProductJpaEntity, UUID> {

  Optional<ProductJpaEntity> findBySku(String sku);

  boolean existsBySku(String sku);
}

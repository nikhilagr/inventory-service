package com.example.inventory.application.command;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateProductCommand(
    @NotBlank String name,
    @NotBlank String sku,
    @Min(0) int quantity,
    @NotNull @DecimalMin(value = "0.01") BigDecimal price) {}

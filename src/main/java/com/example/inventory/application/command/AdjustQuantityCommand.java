package com.example.inventory.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AdjustQuantityCommand(
    @NotBlank @Pattern(regexp = "^[0-9a-fA-F-]{36}$", message = "productId must be a valid UUID") String productId,
    int delta) {}

package com.example.inventory.application.command;

import jakarta.validation.constraints.NotBlank;

public record AdjustQuantityCommand(
    @NotBlank String productId,
    int delta) {}

package com.example.SalesHub.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RetirarDoEstoqueRequest(
        @Min(1) @NotNull
        Long estoqueId,
        @Min(1) @NotNull
        Long quantidade) {}

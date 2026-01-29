package com.example.SalesHub.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record VendaRequest(
        @Min(1) @NotNull
        Long usuarioId,
        @Min(1) @NotNull
        Long produtoId,
        @Min(1) @NotNull
        Long estoqueId,
        @Min(1) @NotNull
        Long quantidade,
        @Min(0) @NotNull
        BigDecimal preco,
        @Min(0) @NotNull @Max(100)
        Double desconto) {}


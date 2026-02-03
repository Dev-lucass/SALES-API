package com.example.SalesHub.dto.request;

import jakarta.validation.constraints.*;
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
        @DecimalMin("1") @DecimalMax("1000000000") @NotNull
        BigDecimal quantidade,
        @DecimalMin("0") @DecimalMax("1000000000") @NotNull
        BigDecimal valor,
        @Min(0) @Max(100) @NotNull
        Double desconto) {}


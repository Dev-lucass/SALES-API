package com.example.SalesHub.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record EstoqueRequest(
                             @Min(1) @NotNull
                             Long produtoId,
                             @DecimalMin("1") @DecimalMax( "1000000000") @NotNull
                             BigDecimal quantidade) {}

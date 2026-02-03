package com.example.SalesHub.dto.projection;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record EstoqueProjection(
        Long id,
        Long produtoId,
        BigDecimal quantidadeInicial,
        BigDecimal quantidadeAtual) {}

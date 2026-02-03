package com.example.SalesHub.dto.filter;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record EstoqueFilter(
        Long id,
        Long produtoId,
        BigDecimal quantidadeInicial,
        BigDecimal quantidadeAtual) { }

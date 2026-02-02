package com.example.SalesHub.dto.projection;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record MetricaProjection(
        Long rank,
        Long produtoId,
        String produto,
        BigDecimal quantidadeRetirada) {}

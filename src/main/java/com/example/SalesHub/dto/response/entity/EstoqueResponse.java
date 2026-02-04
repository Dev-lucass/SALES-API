package com.example.SalesHub.dto.response.entity;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record EstoqueResponse(Long id,
                              Long produtoId,
                              BigDecimal quantidadeInicial,
                              BigDecimal quantidadeAtual) {}

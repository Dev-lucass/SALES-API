package com.example.SalesHub.dto.projection;

import lombok.Builder;

@Builder
public record EstoqueProjection(
        Long id,
        Long produtoId,
        Long quantidadeInicial,
        Long quantidadeAtual) {}

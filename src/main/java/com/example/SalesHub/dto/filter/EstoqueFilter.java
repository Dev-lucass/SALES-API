package com.example.SalesHub.dto.filter;

import lombok.Builder;

@Builder
public record EstoqueFilter(
        Long id,
        Long produtoId,
        Long quantidadeInicial,
        Long quantidadeAtual) { }

package com.example.SalesHub.dto.response.entity;

import lombok.Builder;

@Builder
public record EstoqueResponse( Long id,
                                                              Long produtoId,
                                                              Long quantidadeInicial,
                                                              Long quantidadeAtual) {}

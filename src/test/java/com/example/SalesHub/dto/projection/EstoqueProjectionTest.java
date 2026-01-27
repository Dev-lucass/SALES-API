package com.example.SalesHub.dto.projection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EstoqueProjectionTest {

    @Test
    void deve_criar_estoqueProjection_completo() {
        var estoqueProjection = EstoqueProjection.builder()
                .id(1L)
                .produtoId(10L)
                .quantidadeInicial(100L)
                .quantidadeAtual(50L)
                .build();

        Assertions.assertThat(estoqueProjection)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.id()).isEqualTo(1L);
                    Assertions.assertThat(saida.produtoId()).isEqualTo(10L);
                    Assertions.assertThat(saida.quantidadeInicial()).isEqualTo(100L);
                    Assertions.assertThat(saida.quantidadeAtual()).isEqualTo(50L);
                });
    }

    @Test
    void deve_criar_estoqueProjection_nulo() {
        var estoqueProjection = EstoqueProjection.builder()
                .id(null)
                .produtoId(null)
                .quantidadeInicial(null)
                .quantidadeAtual(null)
                .build();

        Assertions.assertThat(estoqueProjection)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.id()).isNull();
                    Assertions.assertThat(saida.produtoId()).isNull();
                    Assertions.assertThat(saida.quantidadeInicial()).isNull();
                    Assertions.assertThat(saida.quantidadeAtual()).isNull();
                });
    }
}
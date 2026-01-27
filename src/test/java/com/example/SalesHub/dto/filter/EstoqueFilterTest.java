package com.example.SalesHub.dto.filter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EstoqueFilterTest {

    @Test
    void deve_criar_estoqueFilter_completo() {
        var estoqueFilter = EstoqueFilter.builder()
                .id(1L)
                .produtoId(50L)
                .quantidadeInicial(10L)
                .quantidadeAtual(100L)
                .build();

        Assertions.assertThat(estoqueFilter)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.id()).isEqualTo(1L);
                    Assertions.assertThat(saida.produtoId()).isEqualTo(50L);
                    Assertions.assertThat(saida.quantidadeInicial()).isEqualTo(10L);
                    Assertions.assertThat(saida.quantidadeAtual()).isEqualTo(100L);
                });
    }

    @Test
    void deve_criar_estoqueFilter_com_campos_nulos() {
        var estoqueFilter = EstoqueFilter.builder()
                .id(null)
                .produtoId(null)
                .quantidadeInicial(null)
                .quantidadeAtual(null)
                .build();

        Assertions.assertThat(estoqueFilter)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.id()).isNull();
                    Assertions.assertThat(saida.produtoId()).isNull();
                    Assertions.assertThat(saida.quantidadeInicial()).isNull();
                    Assertions.assertThat(saida.quantidadeAtual()).isNull();
                });
    }

    @Test
    void deve_garantir_integridade_do_relacionamento_produtoId() {
        Long produtoIdRelacionamento = 999L;

        var estoqueFilter = EstoqueFilter.builder()
                .produtoId(produtoIdRelacionamento)
                .build();

        Assertions.assertThat(estoqueFilter.produtoId())
                .isNotNull()
                .isEqualTo(produtoIdRelacionamento);
    }

    @Test
    void deve_validar_igualdade_de_valores_long() {
        var valorQuantidade = 500L;

        var estoqueFilter = EstoqueFilter.builder()
                .quantidadeInicial(valorQuantidade)
                .quantidadeAtual(valorQuantidade)
                .build();

        Assertions.assertThat(estoqueFilter.quantidadeInicial())
                .isEqualTo(estoqueFilter.quantidadeAtual())
                .isEqualTo(500L);
    }
}
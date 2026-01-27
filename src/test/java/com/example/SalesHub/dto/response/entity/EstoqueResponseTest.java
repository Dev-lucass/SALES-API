package com.example.SalesHub.dto.response.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EstoqueResponseTest {

    @Test
    void deve_criar_estoqueResponse_completo() {
        var estoqueResponse = EstoqueResponse.builder()
                .id(1L)
                .produtoId(10L)
                .quantidadeInicial(100L)
                .quantidadeAtual(85L)
                .build();

        Assertions.assertThat(estoqueResponse)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.id()).isEqualTo(1L);
                    Assertions.assertThat(saida.produtoId()).isEqualTo(10L);
                    Assertions.assertThat(saida.quantidadeInicial()).isEqualTo(100L);
                    Assertions.assertThat(saida.quantidadeAtual()).isEqualTo(85L);
                });
    }

    @Test
    void deve_criar_estoqueResponse_com_valores_nulos() {
        var estoqueResponse = EstoqueResponse.builder()
                .id(null)
                .produtoId(null)
                .quantidadeInicial(null)
                .quantidadeAtual(null)
                .build();

        Assertions.assertThat(estoqueResponse)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.id()).isNull();
                    Assertions.assertThat(saida.produtoId()).isNull();
                    Assertions.assertThat(saida.quantidadeInicial()).isNull();
                    Assertions.assertThat(saida.quantidadeAtual()).isNull();
                });
    }

    @Test
    void deve_garantir_integridade_dos_dados() {
        var id = 500L;
        var produtoId = 25L;
        var inicial = 1000L;
        var atual = 500L;

        var estoqueResponse = EstoqueResponse.builder()
                .id(id)
                .produtoId(produtoId)
                .quantidadeInicial(inicial)
                .quantidadeAtual(atual)
                .build();

        Assertions.assertThat(estoqueResponse.id()).isEqualTo(id);
        Assertions.assertThat(estoqueResponse.produtoId()).isEqualTo(produtoId);
        Assertions.assertThat(estoqueResponse.quantidadeInicial()).isEqualTo(inicial);
        Assertions.assertThat(estoqueResponse.quantidadeAtual()).isEqualTo(atual);
    }
}
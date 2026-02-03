package com.example.SalesHub.dto.projection;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EstoqueProjectionTest {

    @Test
    void deve_criar_estoque_projection_com_sucesso_usando_builder() {
        var id = 1L;
        var produtoId = 50L;
        var quantidadeInicial = new BigDecimal("100.00");
        var quantidadeAtual = new BigDecimal("85.50");

        var projection = EstoqueProjection.builder()
                .id(id)
                .produtoId(produtoId)
                .quantidadeInicial(quantidadeInicial)
                .quantidadeAtual(quantidadeAtual)
                .build();

        assertThat(projection).isNotNull();
        assertThat(projection.id()).isEqualTo(id);
        assertThat(projection.produtoId()).isEqualTo(produtoId);
        assertThat(projection.quantidadeInicial()).isEqualByComparingTo(quantidadeInicial);
        assertThat(projection.quantidadeAtual()).isEqualByComparingTo(quantidadeAtual);
    }

    @Test
    void deve_garantir_que_todos_os_campos_da_projecao_nao_sao_nulos() {
        var projection = EstoqueProjection.builder()
                .id(1L)
                .produtoId(1L)
                .quantidadeInicial(BigDecimal.ZERO)
                .quantidadeAtual(BigDecimal.ZERO)
                .build();

        assertThat(projection.id()).isNotNull();
        assertThat(projection.produtoId()).isNotNull();
        assertThat(projection.quantidadeInicial()).isNotNull();
        assertThat(projection.quantidadeAtual()).isNotNull();
    }

    @Test
    void deve_testar_igualdade_e_hashcode_do_record_estoque_projection() {
        var p1 = EstoqueProjection.builder()
                .id(1L)
                .produtoId(10L)
                .quantidadeInicial(BigDecimal.TEN)
                .quantidadeAtual(BigDecimal.ONE)
                .build();

        var p2 = EstoqueProjection.builder()
                .id(1L)
                .produtoId(10L)
                .quantidadeInicial(BigDecimal.TEN)
                .quantidadeAtual(BigDecimal.ONE)
                .build();

        assertThat(p1).isEqualTo(p2);
        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
    }
}
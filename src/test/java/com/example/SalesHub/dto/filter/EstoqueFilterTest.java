package com.example.SalesHub.dto.filter;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EstoqueFilterTest {

    @Test
    void deve_criar_estoque_filter_com_sucesso_usando_builder() {
        var id = 1L;
        var produtoId = 10L;
        var quantidadeInicial = new BigDecimal("100.00");
        var quantidadeAtual = new BigDecimal("50.00");

        var filter = EstoqueFilter.builder()
                .id(id)
                .produtoId(produtoId)
                .quantidadeInicial(quantidadeInicial)
                .quantidadeAtual(quantidadeAtual)
                .build();

        assertThat(filter).isNotNull();
        assertThat(filter.id()).isEqualTo(id);
        assertThat(filter.produtoId()).isEqualTo(produtoId);
        assertThat(filter.quantidadeInicial()).isEqualByComparingTo(quantidadeInicial);
        assertThat(filter.quantidadeAtual()).isEqualByComparingTo(quantidadeAtual);
    }

    @Test
    void deve_garantir_que_todos_os_campos_estao_preenchidos() {
        var filter = EstoqueFilter.builder()
                .id(2L)
                .produtoId(20L)
                .quantidadeInicial(BigDecimal.ZERO)
                .quantidadeAtual(BigDecimal.TEN)
                .build();

        assertThat(filter.id()).isNotNull();
        assertThat(filter.produtoId()).isNotNull();
        assertThat(filter.quantidadeInicial()).isNotNull();
        assertThat(filter.quantidadeAtual()).isNotNull();
    }

    @Test
    void deve_testar_igualdade_do_record() {
        var filter1 = EstoqueFilter.builder()
                .id(1L)
                .produtoId(10L)
                .quantidadeInicial(BigDecimal.ONE)
                .quantidadeAtual(BigDecimal.ONE)
                .build();

        var filter2 = EstoqueFilter.builder()
                .id(1L)
                .produtoId(10L)
                .quantidadeInicial(BigDecimal.ONE)
                .quantidadeAtual(BigDecimal.ONE)
                .build();

        assertThat(filter1).isEqualTo(filter2);
        assertThat(filter1.hashCode()).isEqualTo(filter2.hashCode());
    }
}
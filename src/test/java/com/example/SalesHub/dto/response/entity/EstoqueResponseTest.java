package com.example.SalesHub.dto.response.entity;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EstoqueResponseTest {

    @Test
    void deve_criar_estoque_response_com_sucesso_usando_builder() {
        var id = 1L;
        var produtoId = 10L;
        var quantidadeInicial = new BigDecimal("100.00");
        var quantidadeAtual = new BigDecimal("75.00");

        var response = EstoqueResponse.builder()
                .id(id)
                .produtoId(produtoId)
                .quantidadeInicial(quantidadeInicial)
                .quantidadeAtual(quantidadeAtual)
                .build();

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.produtoId()).isEqualTo(produtoId);
        assertThat(response.quantidadeInicial()).isEqualByComparingTo(quantidadeInicial);
        assertThat(response.quantidadeAtual()).isEqualByComparingTo(quantidadeAtual);
    }

    @Test
    void deve_garantir_que_todos_os_campos_da_resposta_nao_sao_nulos() {
        var response = EstoqueResponse.builder()
                .id(1L)
                .produtoId(2L)
                .quantidadeInicial(BigDecimal.ZERO)
                .quantidadeAtual(BigDecimal.TEN)
                .build();

        assertThat(response.id()).isNotNull();
        assertThat(response.produtoId()).isNotNull();
        assertThat(response.quantidadeInicial()).isNotNull();
        assertThat(response.quantidadeAtual()).isNotNull();
    }

    @Test
    void deve_testar_igualdade_e_hashcode_do_record_estoque_response() {
        var r1 = EstoqueResponse.builder()
                .id(1L)
                .produtoId(10L)
                .quantidadeInicial(BigDecimal.ONE)
                .quantidadeAtual(BigDecimal.ONE)
                .build();

        var r2 = EstoqueResponse.builder()
                .id(1L)
                .produtoId(10L)
                .quantidadeInicial(BigDecimal.ONE)
                .quantidadeAtual(BigDecimal.ONE)
                .build();

        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }
}
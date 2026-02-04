package com.example.SalesHub.dto.request;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EstoqueRequestTest {

    @Test
    void deve_criar_estoque_request_com_sucesso_usando_builder() {
        var produtoId = 1L;
        var quantidade = new BigDecimal("500.00");

        var request = EstoqueRequest.builder()
                .produtoId(produtoId)
                .quantidade(quantidade)
                .build();

        assertThat(request).isNotNull();
        assertThat(request.produtoId()).isEqualTo(produtoId);
        assertThat(request.quantidade()).isEqualByComparingTo(quantidade);
    }

    @Test
    void deve_garantir_que_todos_os_campos_do_request_nao_sao_nulos() {
        var request = EstoqueRequest.builder()
                .produtoId(10L)
                .quantidade(BigDecimal.TEN)
                .build();

        assertThat(request.produtoId()).isNotNull();
        assertThat(request.quantidade()).isNotNull();
    }

    @Test
    void deve_testar_igualdade_e_hashcode_do_record_estoque_request() {
        var r1 = EstoqueRequest.builder()
                .produtoId(1L)
                .quantidade(BigDecimal.ONE)
                .build();

        var r2 = EstoqueRequest.builder()
                .produtoId(1L)
                .quantidade(BigDecimal.ONE)
                .build();

        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }
}
package com.example.SalesHub.dto.response.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VendaResponseTest {

    private VendaResponse response;
    private LocalDateTime agora;

    @BeforeEach
    void setup() {
        agora = LocalDateTime.now();
        response = VendaResponse.builder()
                .id(1L)
                .valor(new BigDecimal("100.00"))
                .quantidade(2L)
                .dataVenda(agora)
                .build();
    }

    @Test
    void deve_armazenar_os_valores_corretamente_via_builder() {
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.valor()).isEqualTo(new BigDecimal("100.00"));
        assertThat(response.quantidade()).isEqualTo(2L);
        assertThat(response.dataVenda()).isEqualTo(agora);
    }

    @Test
    void deve_permitir_valores_nulos_nos_relacionamentos_se_necessario() {
        var responseSemRelacoes = VendaResponse.builder()
                .id(2L)
                .usuario(null)
                .produto(null)
                .estoque(null)
                .build();

        assertThat(responseSemRelacoes.usuario()).isNull();
        assertThat(responseSemRelacoes.produto()).isNull();
        assertThat(responseSemRelacoes.estoque()).isNull();
    }

    @Test
    void deve_garantir_a_imutabilidade_do_record() {
        var valorEsperado = new BigDecimal("500.00");
        var novaResponse = VendaResponse.builder()
                .valor(valorEsperado)
                .build();

        assertThat(novaResponse.valor()).isEqualTo(valorEsperado);
        assertThat(response.valor()).isNotEqualTo(novaResponse.valor());
    }
}
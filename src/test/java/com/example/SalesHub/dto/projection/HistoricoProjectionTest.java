package com.example.SalesHub.dto.projection;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HistoricoProjectionTest {

    @Test
    void deve_criar_historico_projection_com_sucesso_usando_builder() {
        var id = 1L;
        var usuarioId = 10L;
        var produtoId = 20L;
        var estoqueId = 30L;
        var quantidade = new BigDecimal("12.50");
        var criadoEm = LocalDateTime.of(2026, 2, 3, 10, 30, 0);

        var projection = HistoricoProjection.builder()
                .id(id)
                .usuarioId(usuarioId)
                .produtoId(produtoId)
                .estoqueId(estoqueId)
                .quantidadeRetirada(quantidade)
                .criadoEm(criadoEm)
                .build();

        assertThat(projection).isNotNull();
        assertThat(projection.id()).isEqualTo(id);
        assertThat(projection.usuarioId()).isEqualTo(usuarioId);
        assertThat(projection.produtoId()).isEqualTo(produtoId);
        assertThat(projection.estoqueId()).isEqualTo(estoqueId);
        assertThat(projection.quantidadeRetirada()).isEqualByComparingTo(quantidade);
        assertThat(projection.criadoEm()).isEqualTo(criadoEm);
    }

    @Test
    void deve_garantir_que_todos_os_campos_da_projecao_historico_nao_sao_nulos() {
        var projection = HistoricoProjection.builder()
                .id(1L)
                .usuarioId(1L)
                .produtoId(1L)
                .estoqueId(1L)
                .quantidadeRetirada(BigDecimal.ONE)
                .criadoEm(LocalDateTime.now())
                .build();

        assertThat(projection.id()).isNotNull();
        assertThat(projection.usuarioId()).isNotNull();
        assertThat(projection.produtoId()).isNotNull();
        assertThat(projection.estoqueId()).isNotNull();
        assertThat(projection.quantidadeRetirada()).isNotNull();
        assertThat(projection.criadoEm()).isNotNull();
    }

    @Test
    void deve_testar_igualdade_e_hashcode_do_record_historico_projection() {
        var agora = LocalDateTime.now();

        var p1 = HistoricoProjection.builder()
                .id(1L)
                .criadoEm(agora)
                .build();

        var p2 = HistoricoProjection.builder()
                .id(1L)
                .criadoEm(agora)
                .build();

        assertThat(p1).isEqualTo(p2);
        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
    }
}
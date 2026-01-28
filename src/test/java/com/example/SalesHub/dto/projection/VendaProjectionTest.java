package com.example.SalesHub.dto.projection;

import com.example.SalesHub.model.enums.StatusPagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VendaProjectionTest {

    private VendaProjection projection;
    private LocalDateTime dataReferencia;

    @BeforeEach
    void setup() {
        dataReferencia = LocalDateTime.of(2026, 1, 29, 12, 0);

        projection = VendaProjection.builder()
                .id(100L)
                .usuarioId(1L)
                .usuario("João")
                .valor(new BigDecimal("250.50"))
                .statusPagamento(StatusPagamento.CONCLUIDO)
                .dataVenda(dataReferencia)
                .build();
    }

    @Test
    void deve_manter_integridade_dos_dados_na_projecao() {
        assertThat(projection.id()).isEqualTo(100L);
        assertThat(projection.usuarioId()).isEqualTo(1L);
        assertThat(projection.usuario()).isEqualTo("João");
        assertThat(projection.valor()).isEqualByComparingTo("250.50");
        assertThat(projection.statusPagamento()).isEqualTo(StatusPagamento.CONCLUIDO);
        assertThat(projection.dataVenda()).isEqualTo(dataReferencia);
    }

    @Test
    void deve_permitir_criacao_com_valores_nulos_para_filtros_opcionais() {
        var projectionNula = VendaProjection.builder()
                .id(200L)
                .usuarioId(null)
                .usuario(null)
                .valor(null)
                .statusPagamento(null)
                .build();

        assertThat(projectionNula.usuarioId()).isNull();
        assertThat(projectionNula.usuario()).isNull();
        assertThat(projectionNula.valor()).isNull();
        assertThat(projectionNula.statusPagamento()).isNull();
    }

    @Test
    void deve_garantir_que_instancias_diferentes_nao_sao_iguais() {
        var outraProjection = VendaProjection.builder()
                .id(101L)
                .build();

        assertThat(projection).isNotEqualTo(outraProjection);
    }
}
package com.example.SalesHub.dto.filter;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VendedorFilterTest {

    @Test
    void deve_criar_vendedor_filter_com_sucesso() {
        var nascimento = LocalDate.of(1990, 1, 1);
        var inicio = LocalDate.of(2026, 1, 1);
        var fim = LocalDate.of(2026, 1, 31);

        var filter = VendedorFilter.builder()
                .id(1L)
                .usuarioId(10L)
                .dataNascimento(nascimento)
                .dataInicial(inicio)
                .dataFinal(fim)
                .build();

        assertThat(filter.id()).isEqualTo(1L);
        assertThat(filter.usuarioId()).isEqualTo(10L);
        assertThat(filter.dataNascimento()).isEqualTo(nascimento);
        assertThat(filter.dataInicial()).isEqualTo(inicio);
        assertThat(filter.dataFinal()).isEqualTo(fim);
    }

    @Test
    void deve_permitir_filtro_vazio() {
        var filter = VendedorFilter.builder().build();

        assertThat(filter.id()).isNull();
        assertThat(filter.usuarioId()).isNull();
        assertThat(filter.dataNascimento()).isNull();
        assertThat(filter.dataInicial()).isNull();
        assertThat(filter.dataFinal()).isNull();
    }
}
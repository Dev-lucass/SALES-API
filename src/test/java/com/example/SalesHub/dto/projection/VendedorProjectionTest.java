package com.example.SalesHub.dto.projection;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VendedorProjectionTest {

    @Test
    void deve_criar_vendedor_projection_com_sucesso() {
        var agora = LocalDateTime.now();
        var nascimento = LocalDate.of(1985, 5, 20);

        var usuarioProj = UsuarioProjection.builder()
                .id(1L)
                .nome("Vendedor Projecao")
                .build();

        var projection = VendedorProjection.builder()
                .id(50L)
                .usuario(usuarioProj)
                .dataNascimento(nascimento)
                .criadoEm(agora)
                .build();

        assertThat(projection.id()).isEqualTo(50L);
        assertThat(projection.usuario()).isNotNull();
        assertThat(projection.usuario().id()).isEqualTo(1L);
        assertThat(projection.dataNascimento()).isEqualTo(nascimento);
        assertThat(projection.criadoEm()).isEqualTo(agora);
    }

    @Test
    void deve_validar_instancia_de_record() {
        var projection = VendedorProjection.builder().build();
        assertThat(projection).isInstanceOf(Record.class);
    }

    @Test
    void deve_permitir_valores_nulos_na_projecao() {
        var projection = VendedorProjection.builder()
                .id(1L)
                .usuario(null)
                .build();

        assertThat(projection.id()).isEqualTo(1L);
        assertThat(projection.usuario()).isNull();
        assertThat(projection.dataNascimento()).isNull();
        assertThat(projection.criadoEm()).isNull();
    }
}
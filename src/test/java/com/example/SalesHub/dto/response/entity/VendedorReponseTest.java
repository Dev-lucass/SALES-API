package com.example.SalesHub.dto.response.entity;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VendedorReponseTest {

    @Test
    void deve_criar_vendedor_response_com_sucesso() {
        var agora = LocalDateTime.now();
        var nascimento = LocalDate.of(1995, 10, 20);
        var usuarioRes = UsuarioResponse.builder()
                .id(1L)
                .nome("Vendedor Teste")
                .build();

        var response = VendedorReponse.builder()
                .id(100L)
                .usuario(usuarioRes)
                .dataNascimento(nascimento)
                .criadoEm(agora)
                .build();

        assertThat(response.id()).isEqualTo(100L);
        assertThat(response.usuario()).isEqualTo(usuarioRes);
        assertThat(response.usuario().nome()).isEqualTo("Vendedor Teste");
        assertThat(response.dataNascimento()).isEqualTo(nascimento);
        assertThat(response.criadoEm()).isEqualTo(agora);
    }

    @Test
    void deve_permitir_campos_nulos_na_resposta() {
        var response = VendedorReponse.builder()
                .id(1L)
                .build();

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.usuario()).isNull();
        assertThat(response.dataNascimento()).isNull();
        assertThat(response.criadoEm()).isNull();
    }

    @Test
    void deve_garantir_que_o_record_seja_imutavel() {
        var response = VendedorReponse.builder()
                .id(1L)
                .build();

        assertThat(response).isInstanceOf(Record.class);
    }
}
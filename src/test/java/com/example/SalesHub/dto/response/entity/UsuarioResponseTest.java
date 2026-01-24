package com.example.SalesHub.dto.response.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UsuarioResponseTest {

    @Test
    void deve_criar_usuarioResponse_completo() {

        var data = LocalDateTime.of(2000, 1, 1, 1, 0, 0);

        var usuarioResponse = UsuarioResponse.builder()
                .id(1L)
                .nome("usuario")
                .email("usuario@gmail.com")
                .criadoEm(data)
                .build();

        Assertions.assertThat(usuarioResponse)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.id()).isEqualTo(1L);
                    Assertions.assertThat(saida.nome()).isEqualTo("usuario");
                    Assertions.assertThat(saida.email()).isEqualTo("usuario@gmail.com");
                    Assertions.assertThat(saida.criadoEm()).isEqualTo(data);
                });
    }

    @Test
    void deve_criar_usuarioResponse_nulo() {
        var usuarioResponse = UsuarioResponse.builder()
                .id(null)
                .nome(null)
                .email(null)
                .criadoEm(null)
                .build();

        Assertions.assertThat(usuarioResponse)
                .satisfies(saida -> {
                    Assertions.assertThat(saida.id()).isNull();
                    Assertions.assertThat(saida.nome()).isNull();
                    Assertions.assertThat(saida.email()).isNull();
                    Assertions.assertThat(saida.criadoEm()).isNull();
                });
    }
}
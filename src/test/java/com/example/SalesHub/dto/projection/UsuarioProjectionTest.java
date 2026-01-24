package com.example.SalesHub.dto.projection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UsuarioProjectionTest {

    @Test
    void deve_criar_usuarioProjection_completo() {

        var data = LocalDateTime.of(2000, 1, 1, 1, 0, 0);

        var usuarioProjection = UsuarioProjection.builder()
                .id(1L)
                .nome("teste")
                .email("teste@gmail.com")
                .criadoEm(data)
                .build();

        Assertions.assertThat(usuarioProjection)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.id()).isEqualTo(1L);
                    Assertions.assertThat(saida.nome()).isEqualTo("teste");
                    Assertions.assertThat(saida.email()).isEqualTo("teste@gmail.com");
                    Assertions.assertThat(saida.criadoEm()).isEqualTo(data);
                });
    }

    @Test
    void deve_criar_usuarioProjection_nulo() {

        var usuarioProjection = UsuarioProjection.builder()
                .id(null)
                .nome(null)
                .email(null)
                .criadoEm(null)
                .build();

        Assertions.assertThat(usuarioProjection)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.id()).isNull();
                    Assertions.assertThat(saida.nome()).isNull();
                    Assertions.assertThat(saida.email()).isNull();
                    Assertions.assertThat(saida.criadoEm()).isNull();
                });
    }
}
package com.example.SalesHub.dto.filter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UsuarioFilterTest {

    @Test
    void deve_criar_usuarioFilter_completo() {

        var inicio = LocalDate.of(2000, 1, 1);
        var fim = LocalDate.of(2000, 1, 10);

        var usuarioFilter = UsuarioFilter.builder()
                .id(1L)
                .nome("teste")
                .email("teste@gmail.com")
                .dataInicial(inicio)
                .dataFinal(fim)
                .build();

        Assertions.assertThat(usuarioFilter)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.id()).isEqualTo(1L);
                    Assertions.assertThat(saida.nome()).isEqualTo("teste");
                    Assertions.assertThat(saida.email()).isEqualTo("teste@gmail.com");
                    Assertions.assertThat(saida.dataInicial()).isEqualTo(inicio);
                    Assertions.assertThat(saida.dataFinal()).isEqualTo(fim);
                });
    }

    @Test
    void deve_criar_usuarioFilter_nulo() {

        var usuarioFilter = UsuarioFilter.builder()
                .id(null)
                .nome(null)
                .email(null)
                .dataInicial(null)
                .dataFinal(null)
                .build();

        Assertions.assertThat(usuarioFilter)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.id()).isNull();
                    Assertions.assertThat(saida.nome()).isNull();
                    Assertions.assertThat(saida.email()).isNull();
                    Assertions.assertThat(saida.dataInicial()).isNull();
                    Assertions.assertThat(saida.dataFinal()).isNull();
                });
    }

}
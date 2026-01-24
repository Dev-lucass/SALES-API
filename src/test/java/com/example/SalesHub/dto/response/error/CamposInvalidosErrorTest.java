package com.example.SalesHub.dto.response.error;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CamposInvalidosErrorTest {

    @Test
    void deve_criar_camposInvalidosError_completo() {

        var campo = "email";
        var mensagem = "E-mail inválido";

        var erro = CamposInvalidosError.builder()
                .campo(campo)
                .erro(mensagem)
                .build();

        Assertions.assertThat(erro)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.campo()).isEqualTo(campo);
                    Assertions.assertThat(saida.erro()).isEqualTo(mensagem);
                });
    }

    @Test
    void deve_criar_camposInvalidosError_nulo() {

        var erro = CamposInvalidosError.builder()
                .campo(null)
                .erro(null)
                .build();

        Assertions.assertThat(erro)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.campo()).isNull();
                    Assertions.assertThat(saida.erro()).isNull();
                });
    }

}
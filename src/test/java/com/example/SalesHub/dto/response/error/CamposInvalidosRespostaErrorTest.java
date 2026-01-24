package com.example.SalesHub.dto.response.error;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CamposInvalidosRespostaErrorTest {

    @Test
    void deve_criar_camposInvalidosRespostaError_completo() {

        var agora = LocalDateTime.now();
        var errosInternos = List.of(
                CamposInvalidosError.builder().campo("nome").erro("Erro nome").build(),
                CamposInvalidosError.builder().campo("email").erro("Erro email").build()
        );

        var resposta = CamposInvalidosRespostaError.builder()
                .status(400)
                .erro("Erro de validação")
                .camposInvalidos(errosInternos)
                .data(agora)
                .build();

        Assertions.assertThat(resposta)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.status()).isEqualTo(400);
                    Assertions.assertThat(saida.erro()).isEqualTo("Erro de validação");
                    Assertions.assertThat(saida.data()).isEqualTo(agora);
                    Assertions.assertThat(saida.camposInvalidos())
                            .hasSize(2)
                            .containsAll(errosInternos);
                });
    }

    @Test
    void deve_criar_camposInvalidosRespostaError_nulo() {

        var resposta = CamposInvalidosRespostaError.builder()
                .status(0)
                .erro(null)
                .camposInvalidos(null)
                .data(null)
                .build();

        Assertions.assertThat(resposta)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.status()).isZero();
                    Assertions.assertThat(saida.erro()).isNull();
                    Assertions.assertThat(saida.camposInvalidos()).isNull();
                    Assertions.assertThat(saida.data()).isNull();
                });
    }

}
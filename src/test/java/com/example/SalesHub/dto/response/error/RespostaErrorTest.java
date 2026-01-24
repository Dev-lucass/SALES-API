package com.example.SalesHub.dto.response.error;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class RespostaErrorTest {

    @Test
    void deve_criar_respostaError_completo() {

        var agora = LocalDateTime.now();
        var status = 404;
        var mensagemErro = "Recurso não encontrado";

        var resposta = RespostaError.builder()
                .status(status)
                .erro(mensagemErro)
                .data(agora)
                .build();

        Assertions.assertThat(resposta)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.status()).isEqualTo(status);
                    Assertions.assertThat(saida.erro()).isEqualTo(mensagemErro);
                    Assertions.assertThat(saida.data()).isEqualTo(agora);
                });
    }

    @Test
    void deve_criar_respostaError_nulo() {

        var resposta = RespostaError.builder()
                .status(0)
                .erro(null)
                .data(null)
                .build();

        Assertions.assertThat(resposta)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.status()).isZero();
                    Assertions.assertThat(saida.erro()).isNull();
                    Assertions.assertThat(saida.data()).isNull();
                });
    }

}
package com.example.SalesHub.dto.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class RetirarDoEstoqueRequestTest {

    private final Validator validar = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void deve_criar_retirarDoEstoqueRequest_completo() {
        var request = RetirarDoEstoqueRequest.builder()
                .estoqueId(100L)
                .quantidade(5L)
                .build();

        Assertions.assertThat(request)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.estoqueId()).isEqualTo(100L);
                    Assertions.assertThat(saida.quantidade()).isEqualTo(5L);
                });
    }

    @Test
    void deve_criar_retirarDoEstoqueRequest_nulo() {
        var request = RetirarDoEstoqueRequest.builder()
                .estoqueId(null)
                .quantidade(null)
                .build();

        Assertions.assertThat(request)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.estoqueId()).isNull();
                    Assertions.assertThat(saida.quantidade()).isNull();
                });
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {0L, -1L, -50L})
    void deve_validar_estoqueId(Long estoqueIdInvalido) {
        var request = RetirarDoEstoqueRequest.builder()
                .estoqueId(estoqueIdInvalido)
                .quantidade(10L)
                .build();

        var violacoes = validar.validate(request);

        Assertions.assertThat(violacoes)
                .isNotEmpty()
                .extracting(saida -> saida.getPropertyPath().toString())
                .containsOnly("estoqueId");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {0L, -1L, -99L})
    void deve_validar_quantidade(Long quantidadeInvalida) {
        var request = RetirarDoEstoqueRequest.builder()
                .estoqueId(1L)
                .quantidade(quantidadeInvalida)
                .build();

        var violacoes = validar.validate(request);

        Assertions.assertThat(violacoes)
                .isNotEmpty()
                .extracting(saida -> saida.getPropertyPath().toString())
                .containsOnly("quantidade");
    }
}
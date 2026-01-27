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
class EstoqueRequestTest {

    private final Validator validar = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void deve_criar_estoqueRequest_completo() {
        var estoqueRequest = EstoqueRequest.builder()
                .produtoId(1L)
                .quantidade(10L)
                .build();

        Assertions.assertThat(estoqueRequest)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.produtoId()).isEqualTo(1L);
                    Assertions.assertThat(saida.quantidade()).isEqualTo(10L);
                });
    }

    @Test
    void deve_criar_estoqueRequest_nulo() {
        var estoqueRequest = EstoqueRequest.builder()
                .produtoId(null)
                .quantidade(null)
                .build();

        Assertions.assertThat(estoqueRequest)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.produtoId()).isNull();
                    Assertions.assertThat(saida.quantidade()).isNull();
                });
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {0L, -1L, -100L})
    void deve_validar_produtoId(Long produtoIdInvalido) {
        var estoqueRequest = EstoqueRequest.builder()
                .produtoId(produtoIdInvalido)
                .quantidade(10L)
                .build();

        var violacao = validar.validate(estoqueRequest);

        Assertions.assertThat(violacao)
                .isNotEmpty()
                .extracting(saida -> saida.getPropertyPath().toString())
                .containsOnly("produtoId");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {0L, -1L, -100L})
    void deve_validar_quantidade(Long quantidadeInvalida) {
        var estoqueRequest = EstoqueRequest.builder()
                .produtoId(1L)
                .quantidade(quantidadeInvalida)
                .build();

        var violacao = validar.validate(estoqueRequest);

        Assertions.assertThat(violacao)
                .isNotEmpty()
                .extracting(saida -> saida.getPropertyPath().toString())
                .containsOnly("quantidade");
    }
}
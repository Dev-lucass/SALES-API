package com.example.SalesHub.dto.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProdutoRequestTest {

    private final Validator validar = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void deve_criar_produtoRequest_completo() {
        var produtoRequest = ProdutoRequest.builder()
                .nome("Teclado Mecânico")
                .descricao("Teclado RGB switch blue")
                .build();

        Assertions.assertThat(produtoRequest)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.nome()).isEqualTo("Teclado Mecânico");
                    Assertions.assertThat(saida.descricao()).isEqualTo("Teclado RGB switch blue");
                });
    }

    @Test
    void deve_criar_produtoRequest_nulo() {
        var produtoRequest = ProdutoRequest.builder()
                .nome(null)
                .descricao(null)
                .build();

        Assertions.assertThat(produtoRequest)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.nome()).isNull();
                    Assertions.assertThat(saida.descricao()).isNull();
                });
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "a"})
    @NullAndEmptySource
    void deve_validar_nome(String nomeInvalido) {
        var produtoRequest = ProdutoRequest.builder()
                .nome(nomeInvalido)
                .descricao("Descrição válida")
                .build();

        var violacao = validar.validate(produtoRequest);

        Assertions.assertThat(violacao)
                .isNotEmpty()
                .extracting(saida -> saida.getPropertyPath().toString())
                .containsOnly("nome");
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "d"})
    @NullAndEmptySource
    void deve_validar_descricao(String descricaoInvalida) {
        var produtoRequest = ProdutoRequest.builder()
                .nome("Nome Válido")
                .descricao(descricaoInvalida)
                .build();

        var violacao = validar.validate(produtoRequest);

        Assertions.assertThat(violacao)
                .isNotEmpty()
                .extracting(saida -> saida.getPropertyPath().toString())
                .containsOnly("descricao");
    }
}
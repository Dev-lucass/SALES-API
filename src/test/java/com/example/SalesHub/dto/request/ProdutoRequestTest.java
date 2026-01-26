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
import java.math.BigDecimal;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProdutoRequestTest {

    private final Validator validar = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void deve_criar_produtoRequest_completo() {
        var produtoRequest = ProdutoRequest.builder()
                .nome("Teclado Mecânico")
                .descricao("Teclado RGB switch blue")
                .preco(BigDecimal.valueOf(250.00))
                .build();

        Assertions.assertThat(produtoRequest)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.nome()).isEqualTo("Teclado Mecânico");
                    Assertions.assertThat(saida.descricao()).isEqualTo("Teclado RGB switch blue");
                    Assertions.assertThat(saida.preco())
                            .usingComparator(BigDecimal::compareTo)
                            .isEqualTo(BigDecimal.valueOf(250.00));
                });
    }

    @Test
    void deve_criar_produtoRequest_nulo() {
        var produtoRequest = ProdutoRequest.builder()
                .nome(null)
                .descricao(null)
                .preco(null)
                .build();

        Assertions.assertThat(produtoRequest)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.nome()).isNull();
                    Assertions.assertThat(saida.descricao()).isNull();
                    Assertions.assertThat(saida.preco()).isNull();
                });
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "a"})
    @NullAndEmptySource
    void deve_validar_nome(String nomeInvalido) {
        var produtoRequest = ProdutoRequest.builder()
                .nome(nomeInvalido)
                .descricao("Descrição válida")
                .preco(BigDecimal.TEN)
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
                .preco(BigDecimal.TEN)
                .build();

        var violacao = validar.validate(produtoRequest);

        Assertions.assertThat(violacao)
                .isNotEmpty()
                .extracting(saida -> saida.getPropertyPath().toString())
                .containsOnly("descricao");
    }

    @Test
    void deve_validar_preco_nulo() {
        var produtoRequest = ProdutoRequest.builder()
                .nome("Nome Válido")
                .descricao("Descrição Válida")
                .preco(null)
                .build();

        var violacao = validar.validate(produtoRequest);

        Assertions.assertThat(violacao)
                .isNotEmpty()
                .extracting(saida -> saida.getPropertyPath().toString())
                .containsOnly("preco");
    }

    @Test
    void deve_validar_preco_negativo() {
        var produtoRequest = ProdutoRequest.builder()
                .nome("Nome Válido")
                .descricao("Descrição Válida")
                .preco(BigDecimal.valueOf(-1.00))
                .build();

        var violacao = validar.validate(produtoRequest);

        Assertions.assertThat(violacao)
                .isNotEmpty()
                .extracting(saida -> saida.getPropertyPath().toString())
                .containsOnly("preco");
    }
}
package com.example.SalesHub.dto.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VendaRequestTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private VendaRequest request;

    @BeforeEach
    void setup() {
        request = VendaRequest.builder()
                .usuarioId(1L)
                .produtoId(1L)
                .estoqueId(1L)
                .quantidade(10L)
                .valor(BigDecimal.valueOf(100.0))
                .desconto(5.0)
                .build();
    }

    @Test
    void deve_validar_dto_quando_todos_os_campos_estao_corretos() {
        var violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @NullSource
    void deve_invalidar_usuario_id_quando_nulo(Long id) {
        var invalidRequest = VendaRequest.builder()
                .usuarioId(id)
                .produtoId(1L)
                .estoqueId(1L)
                .quantidade(10L)
                .valor(BigDecimal.TEN)
                .desconto(0.0)
                .build();

        var violations = validator.validate(invalidRequest);
        assertThat(violations).hasSize(1);
    }

    @ParameterizedTest
    @NullSource
    void deve_invalidar_produto_id_quando_nulo(Long id) {
        var invalidRequest = VendaRequest.builder()
                .usuarioId(1L)
                .produtoId(id)
                .estoqueId(1L)
                .quantidade(10L)
                .valor(BigDecimal.TEN)
                .desconto(0.0)
                .build();

        var violations = validator.validate(invalidRequest);
        assertThat(violations).hasSize(1);
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1})
    void deve_invalidar_quantidade_quando_valor_for_menor_que_um(long valor) {
        var invalidRequest = VendaRequest.builder()
                .usuarioId(1L)
                .produtoId(1L)
                .estoqueId(1L)
                .quantidade(valor)
                .valor(BigDecimal.TEN)
                .desconto(0.0)
                .build();

        var violations = validator.validate(invalidRequest);
        assertThat(violations).hasSize(1);
    }

    @Test
    void deve_invalidar_preco_quando_valor_for_negativo() {
        var invalidRequest = VendaRequest.builder()
                .usuarioId(1L)
                .produtoId(1L)
                .estoqueId(1L)
                .quantidade(10L)
                .valor(new BigDecimal("-1.0"))
                .desconto(0.0)
                .build();

        var violations = validator.validate(invalidRequest);
        assertThat(violations).hasSize(1);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, 101.0})
    void deve_invalidar_desconto_quando_fora_da_faixa_permitida(double valor) {
        var invalidRequest = VendaRequest.builder()
                .usuarioId(1L)
                .produtoId(1L)
                .estoqueId(1L)
                .quantidade(10L)
                .valor(BigDecimal.TEN)
                .desconto(valor)
                .build();

        var violations = validator.validate(invalidRequest);
        assertThat(violations).hasSize(1);
    }

    @ParameterizedTest
    @NullSource
    void deve_invalidar_estoque_id_quando_nulo(Long id) {
        var invalidRequest = VendaRequest.builder()
                .usuarioId(1L)
                .produtoId(1L)
                .estoqueId(id)
                .quantidade(10L)
                .valor(BigDecimal.TEN)
                .desconto(0.0)
                .build();

        var violations = validator.validate(invalidRequest);
        assertThat(violations).hasSize(1);
    }
}
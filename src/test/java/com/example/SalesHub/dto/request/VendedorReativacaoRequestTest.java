package com.example.SalesHub.dto.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VendedorReativacaoRequestTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void deve_validar_request_de_reativacao_com_sucesso() {
        var request = VendedorReativacaoRequest.builder()
                .vendedorId(1L)
                .cpf("11144477735")
                .build();

        var violacoes = validator.validate(request);

        assertThat(violacoes).isEmpty();
    }

    @Test
    void nao_deve_aceitar_vendedor_id_menor_que_um() {
        var request = VendedorReativacaoRequest.builder()
                .vendedorId(0L)
                .cpf("11144477735")
                .build();

        var violacoes = validator.validate(request);

        assertThat(violacoes).isNotEmpty();
        assertThat(violacoes.iterator().next().getPropertyPath().toString()).isEqualTo("vendedorId");
    }

    @Test
    void nao_deve_aceitar_cpf_com_formato_invalido() {
        var request = VendedorReativacaoRequest.builder()
                .vendedorId(1L)
                .cpf("11122233344")
                .build();

        var violacoes = validator.validate(request);

        assertThat(violacoes).isNotEmpty();
        assertThat(violacoes.iterator().next().getPropertyPath().toString()).isEqualTo("cpf");
    }

    @Test
    void nao_deve_aceitar_campos_nulos() {
        var request = VendedorReativacaoRequest.builder()
                .vendedorId(null)
                .cpf(null)
                .build();

        var violacoes = validator.validate(request);

        assertThat(violacoes).hasSize(2);
    }
}
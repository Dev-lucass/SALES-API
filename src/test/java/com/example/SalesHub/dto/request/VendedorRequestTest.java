package com.example.SalesHub.dto.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VendedorRequestTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void deve_validar_request_com_sucesso() {
        var request = VendedorRequest.builder()
                .usuarioId(1L)
                .cpf("11144477735")
                .dataNascimento(LocalDate.of(1990, 5, 15))
                .build();

        var violacoes = validator.validate(request);

        assertThat(violacoes).isEmpty();
    }

    @Test
    void nao_deve_aceitar_cpf_invalido() {
        var request = VendedorRequest.builder()
                .usuarioId(1L)
                .cpf("11122233344")
                .dataNascimento(LocalDate.of(1990, 5, 15))
                .build();

        var violacoes = validator.validate(request);

        assertThat(violacoes).isNotEmpty();
        assertThat(violacoes.iterator().next().getMessageTemplate()).contains("CPF");
    }

    @Test
    void nao_deve_aceitar_data_de_nascimento_no_futuro() {
        var request = VendedorRequest.builder()
                .usuarioId(1L)
                .cpf("11144477735")
                .dataNascimento(LocalDate.now().plusDays(1))
                .build();

        var violacoes = validator.validate(request);

        assertThat(violacoes).isNotEmpty();
        assertThat(violacoes.iterator().next().getPropertyPath().toString()).isEqualTo("dataNascimento");
    }

    @Test
    void nao_deve_aceitar_usuario_id_nulo_ou_menor_que_um() {
        var request = VendedorRequest.builder()
                .usuarioId(0L)
                .cpf("11144477735")
                .dataNascimento(LocalDate.of(1995, 1, 1))
                .build();

        var violacoes = validator.validate(request);

        assertThat(violacoes).isNotEmpty();
    }

    @Test
    void nao_deve_aceitar_campos_em_branco() {
        var request = VendedorRequest.builder()
                .usuarioId(1L)
                .cpf("")
                .dataNascimento(null)
                .build();

        var violacoes = validator.validate(request);

        assertThat(violacoes).hasSize(3);
    }
}
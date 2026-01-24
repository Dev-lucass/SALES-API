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
class UsuarioRequestTest {

    private final Validator validar = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void deve_criar_usuarioRequest_completo(){
        var usuarioRequest = UsuarioRequest.builder()
                .nome("teste")
                .email("teste@gmail.com")
                .senha("321")
                .build();

        Assertions.assertThat(usuarioRequest)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.nome()).isEqualTo("teste");
                    Assertions.assertThat(saida.email()).isEqualTo("teste@gmail.com");
                    Assertions.assertThat(saida.senha()).isEqualTo("321");
                });
    }

    @Test
    void deve_criar_usuarioRequest_nulo(){
        var usuarioRequest = UsuarioRequest.builder()
                .nome(null)
                .email(null)
                .senha(null)
                .build();

        Assertions.assertThat(usuarioRequest)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.nome()).isNull();
                    Assertions.assertThat(saida.email()).isNull();
                    Assertions.assertThat(saida.senha()).isNull();
                });
    }

    @ParameterizedTest
    @ValueSource(strings = " ")
    @NullAndEmptySource
    void deve_validar_nome(String nomeInvalido){

        var usuarioRequest = UsuarioRequest.builder()
                .nome(nomeInvalido)
                .email("teste@gmail.com")
                .senha("321")
                .build();

        var violacao = validar.validate(usuarioRequest);

        Assertions.assertThat(violacao)
                .isNotEmpty()
                .extracting(saida -> saida.getPropertyPath().toString())
                .containsOnly("nome");
    }

    @ParameterizedTest
    @ValueSource(strings = {" ","@","123"})
    @NullAndEmptySource
    void deve_validar_email(String emailInvalido){

        var usuarioRequest = UsuarioRequest.builder()
                .nome("teste")
                .email(emailInvalido)
                .senha("321")
                .build();

        var violacao = validar.validate(usuarioRequest);

        Assertions.assertThat(violacao)
                .isNotEmpty()
                .extracting(saida -> saida.getPropertyPath().toString())
                .containsOnly("email");
    }

    @ParameterizedTest
    @ValueSource(strings = " ")
    @NullAndEmptySource
    void deve_validar_senha(String senhaInvalida){

        var usuarioRequest = UsuarioRequest.builder()
                .nome("teste")
                .email("teste@gmail.com")
                .senha(senhaInvalida)
                .build();

        var violacao = validar.validate(usuarioRequest);

        Assertions.assertThat(violacao)
                .isNotEmpty()
                .extracting(saida -> saida.getPropertyPath().toString())
                .containsOnly("senha");
    }
}
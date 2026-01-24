package com.example.SalesHub.mapper;

import com.example.SalesHub.dto.request.UsuarioRequest;
import com.example.SalesHub.model.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UsuarioMapperTest {

    private final UsuarioMapper mapper = new UsuarioMapper();

    @Test
    void deve_converter_request_para_entidade() {

        var request = new UsuarioRequest("João", "joao@email.com", "senha123");

        var entidade = mapper.toEntity(request);

        Assertions.assertThat(entidade)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.getNome()).isEqualTo(request.nome());
                    Assertions.assertThat(saida.getEmail()).isEqualTo(request.email());
                    Assertions.assertThat(saida.getSenha()).isEqualTo(request.senha());
                });
    }

    @Test
    void deve_converter_entidade_para_response() {

        var agora = LocalDateTime.now();
        var usuario = Usuario.builder()
                .id(1L)
                .nome("João")
                .email("joao@email.com")
                .criadoEm(agora)
                .build();

        var response = mapper.toResponse(usuario);

        Assertions.assertThat(response)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.id()).isEqualTo(usuario.getId());
                    Assertions.assertThat(saida.nome()).isEqualTo(usuario.getNome());
                    Assertions.assertThat(saida.email()).isEqualTo(usuario.getEmail());
                    Assertions.assertThat(saida.criadoEm()).isEqualTo(usuario.getCriadoEm());
                });
    }
}
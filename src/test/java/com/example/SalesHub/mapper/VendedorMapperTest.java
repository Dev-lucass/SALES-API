package com.example.SalesHub.mapper;

import com.example.SalesHub.dto.request.VendedorRequest;
import com.example.SalesHub.dto.response.entity.UsuarioResponse;
import com.example.SalesHub.model.Usuario;
import com.example.SalesHub.model.Vendedor;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VendedorMapperTest {

    private final VendedorMapper mapper = new VendedorMapper();

    @Test
    void deve_converter_request_para_entidade_com_sucesso() {
        var nascimento = LocalDate.of(1990, 1, 1);
        var request = VendedorRequest.builder()
                .usuarioId(1L)
                .cpf("71430935002")
                .dataNascimento(nascimento)
                .build();

        var usuario = Usuario.builder().id(1L).nome("Usuario Teste").build();

        var resultado = mapper.toEntity(request, usuario);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getUsuario()).isEqualTo(usuario);
        assertThat(resultado.getCpf()).isEqualTo("71430935002");
        assertThat(resultado.getDataNascimento()).isEqualTo(nascimento);
        assertThat(resultado.getCriadoEm()).isNotNull();
        assertThat(resultado.getCriadoEm()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void deve_converter_entidade_para_response_com_sucesso() {
        var agora = LocalDateTime.now();
        var nascimento = LocalDate.of(1990, 1, 1);

        var vendedor = Vendedor.builder()
                .id(10L)
                .cpf("71430935002")
                .dataNascimento(nascimento)
                .criadoEm(agora)
                .build();

        var usuarioResponse = UsuarioResponse.builder()
                .id(1L)
                .nome("Usuario Teste")
                .build();

        var resultado = mapper.toResponse(vendedor, usuarioResponse);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(10L);
        assertThat(resultado.usuario()).isEqualTo(usuarioResponse);
        assertThat(resultado.dataNascimento()).isEqualTo(nascimento);
        assertThat(resultado.criadoEm()).isEqualTo(agora);
    }

    @Test
    void deve_lidar_com_objetos_nulos_no_mapeamento_de_entidade() {
        var request = VendedorRequest.builder().cpf("123").build();

        var resultado = mapper.toEntity(request, null);

        assertThat(resultado.getUsuario()).isNull();
        assertThat(resultado.getCpf()).isEqualTo("123");
    }

    @Test
    void deve_lidar_com_objetos_nulos_no_mapeamento_de_response() {
        var vendedor = Vendedor.builder().id(1L).build();

        var resultado = mapper.toResponse(vendedor, null);

        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.usuario()).isNull();
    }
}
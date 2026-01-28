package com.example.SalesHub.service;

import com.example.SalesHub.dto.filter.UsuarioFilter;
import com.example.SalesHub.dto.projection.UsuarioProjection;
import com.example.SalesHub.dto.request.UsuarioRequest;
import com.example.SalesHub.dto.response.entity.UsuarioResponse;
import com.example.SalesHub.exception.EntidadeDuplicadaException;
import com.example.SalesHub.exception.EntidadeNaoEncontradaException;
import com.example.SalesHub.mapper.UsuarioMapper;
import com.example.SalesHub.model.Usuario;
import com.example.SalesHub.repository.customImpl.UsuarioRepositoryImpl;
import com.example.SalesHub.repository.jpa.UsuarioRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioMapper mapper;

    @Mock
    private UsuarioRepositoryImpl customRepository;

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;

    @Test
    void deve_salvar_usuario_com_sucesso() {
        var request = UsuarioRequest.builder()
                .nome("Teste")
                .email("teste@email.com")
                .senha("123")
                .build();

        var usuario = Usuario.builder().nome("Teste").build();
        var response = UsuarioResponse.builder().id(1L).nome("Teste").build();

        when(mapper.toEntity(request)).thenReturn(usuario);
        when(customRepository.buscarUsuarioDuplicado(usuario)).thenReturn(Optional.empty());
        when(repository.save(usuario)).thenReturn(usuario);
        when(mapper.toResponse(usuario)).thenReturn(response);

        var resultado = service.salvar(request);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
        verify(repository).save(usuario);
    }

    @Test
    void deve_lancar_excecao_ao_salvar_usuario_duplicado() {
        var request = UsuarioRequest.builder().email("teste@email.com").build();
        var usuario = Usuario.builder().email("teste@email.com").build();

        when(mapper.toEntity(request)).thenReturn(usuario);
        when(customRepository.buscarUsuarioDuplicado(usuario))
                .thenReturn(Optional.of(Usuario.builder().build()));

        assertThatThrownBy(() -> service.salvar(request))
                .isInstanceOf(EntidadeDuplicadaException.class)
                .hasMessage("Usuario ja cadastrado");

        verify(repository, never()).save(any());
    }

    @Test
    void deve_buscar_usuarios_paginado() {
        var filter = UsuarioFilter.builder().build();
        var pageable = mock(Pageable.class);
        var pagedResponse = new PageImpl<UsuarioProjection>(List.of());

        when(customRepository.buscarUsuarios(filter, pageable)).thenReturn(pagedResponse);

        var resultado = service.buscar(filter, pageable);

        assertThat(resultado).isNotNull();
        verify(customRepository).buscarUsuarios(filter, pageable);
    }

    @Test
    void deve_atualizar_usuario_com_sucesso() {
        var id = 1L;
        var request = UsuarioRequest.builder()
                .nome("Novo Nome")
                .email("novo@email.com")
                .senha("321")
                .build();

        var usuarioExistente = Usuario.builder()
                .id(id)
                .email("antigo@email.com")
                .build();

        when(customRepository.buscarUsuarioExistente(id)).thenReturn(Optional.of(usuarioExistente));
        when(customRepository.buscarUsuarioDuplicado(usuarioExistente)).thenReturn(Optional.empty());

        service.atualizar(id, request);

        assertThat(usuarioExistente.getNome()).isEqualTo("Novo Nome");
        assertThat(usuarioExistente.getEmail()).isEqualTo("novo@email.com");
        verify(repository).save(usuarioExistente);
    }

    @Test
    void deve_lancar_excecao_ao_buscar_usuario_inexistente() {
        var id = 1L;
        when(customRepository.buscarUsuarioExistente(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.desativar(id))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessage("Usuario não encontrado");
    }

    @Test
    void deve_desativar_usuario_com_sucesso() {
        var id = 1L;
        var usuario = Usuario.builder().ativo(true).build();

        when(customRepository.buscarUsuarioExistente(id)).thenReturn(Optional.of(usuario));

        service.desativar(id);

        assertThat(usuario.getAtivo()).isFalse();
    }

    @Test
    void deve_mapear_usuario_para_response_com_sucesso() {
        var usuario = Usuario.builder().id(1L).nome("Teste").build();
        var response = UsuarioResponse.builder().id(1L).nome("Teste").build();

        when(mapper.toResponse(usuario)).thenReturn(response);

        var resultado = service.mapearUsuario(usuario);

        assertThat(resultado).isNotNull();
        assertThat(resultado.nome()).isEqualTo("Teste");
        verify(mapper).toResponse(usuario);
    }

    @Test
    void deve_buscar_usuario_existente_com_sucesso() {
        var id = 1L;
        var usuario = Usuario.builder().id(id).build();

        when(customRepository.buscarUsuarioExistente(id)).thenReturn(Optional.of(usuario));

        var resultado = service.buscarUsuarioExistente(id);

        assertThat(resultado).isEqualTo(usuario);
        verify(customRepository).buscarUsuarioExistente(id);
    }
}
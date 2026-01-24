package com.example.SalesHub.service;

import com.example.SalesHub.dto.filter.UsuarioFilter;
import com.example.SalesHub.dto.projection.UsuarioProjection;
import com.example.SalesHub.dto.request.UsuarioRequest;
import com.example.SalesHub.dto.response.entity.UsuarioResponse;
import com.example.SalesHub.exception.UsuarioDuplicadoException;
import com.example.SalesHub.exception.UsuarioNaoEncontradoException;
import com.example.SalesHub.mapper.UsuarioMapper;
import com.example.SalesHub.model.Usuario;
import com.example.SalesHub.predicate.UsuarioRepositoryCustom;
import com.example.SalesHub.repository.UsuarioRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioMapper mapper;

    @Mock
    private UsuarioRepositoryCustom customRepository;

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;

    @Test
    void deve_salvar_usuario_com_sucesso() {
        var request = new UsuarioRequest("Teste", "teste@email.com", "123");
        var usuario = new Usuario();
        var response = UsuarioResponse.builder().id(1L).nome("Teste").build();

        Mockito.when(mapper.toEntity(request))
                .thenReturn(usuario);

        Mockito.when(customRepository.buscarUsuarioDuplicado(usuario))
                .thenReturn(Optional.empty());

        Mockito.when(repository.save(usuario))
                .thenReturn(usuario);

        Mockito.when(mapper.toResponse(usuario))
                .thenReturn(response);

        var resultado = service.salvar(request);

        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado.id()).isEqualTo(1L);
        Mockito.verify(repository).save(usuario);
    }

    @Test
    void deve_lancar_excecao_ao_salvar_usuario_duplicado() {
        var request = new UsuarioRequest("Teste", "teste@email.com", "123");
        var usuario = new Usuario();

        Mockito.when(mapper.toEntity(request))
                .thenReturn(usuario);

        Mockito.when(customRepository.buscarUsuarioDuplicado(usuario))
                .thenReturn(Optional.of(new Usuario()));

        Assertions.assertThatThrownBy(() -> service.salvar(request))
                .isInstanceOf(UsuarioDuplicadoException.class)
                .hasMessage("Usuario ja cadastrado");

        Mockito.verify(repository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void deve_buscar_usuarios_paginado() {
        var filter = UsuarioFilter.builder().build();
        var pageable = Mockito.mock(Pageable.class);
        var pagedResponse = new PageImpl<UsuarioProjection>(List.of());

        Mockito.when(customRepository.buscarUsuarios(filter, pageable))
                .thenReturn(pagedResponse);

        var resultado = service.buscar(filter, pageable);

        Assertions.assertThat(resultado).isNotNull();
        Mockito.verify(customRepository).buscarUsuarios(filter, pageable);
    }

    @Test
    void deve_atualizar_usuario_com_sucesso() {
        var id = 1L;
        var request = new UsuarioRequest("Novo Nome", "novo@email.com", "321");
        var usuarioExistente = new Usuario();
        var response = UsuarioResponse.builder().id(id).nome("Novo Nome").build();

        Mockito.when(mapper.toEntity(request))
                .thenReturn(new Usuario());

        Mockito.when(customRepository.buscarUsuarioDuplicado(Mockito.any()))
                .thenReturn(Optional.empty());

        Mockito.when(customRepository.buscarUsuarioExistente(id))
                .thenReturn(Optional.of(usuarioExistente));

        Mockito.when(repository.save(usuarioExistente))
                .thenReturn(usuarioExistente);

        Mockito.when(mapper.toResponse(usuarioExistente))
                .thenReturn(response);

        var resultado = service.atualizar(id, request);

        Assertions.assertThat(resultado.nome()).isEqualTo("Novo Nome");
        Mockito.verify(repository).save(usuarioExistente);
    }

    @Test
    void deve_lancar_excecao_ao_buscar_usuario_inexistente() {
        var id = 1L;
        Mockito.when(customRepository.buscarUsuarioExistente(id))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.desativar(id))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage("Usuario não encontrado");
    }

    @Test
    void deve_desativar_usuario_com_sucesso() {
        var id = 1L;
        var usuario = new Usuario();
        usuario.setAtivo(true);

        Mockito.when(customRepository.buscarUsuarioExistente(id))
                .thenReturn(Optional.of(usuario));

        service.desativar(id);
        Assertions.assertThat(usuario.getAtivo()).isFalse();
    }
}
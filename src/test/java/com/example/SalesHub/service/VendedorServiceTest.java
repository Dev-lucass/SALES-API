package com.example.SalesHub.service;

import com.example.SalesHub.dto.filter.VendedorFilter;
import com.example.SalesHub.dto.projection.VendedorProjection;
import com.example.SalesHub.dto.request.VendedorReativacaoRequest;
import com.example.SalesHub.dto.request.VendedorRequest;
import com.example.SalesHub.dto.response.entity.UsuarioResponse;
import com.example.SalesHub.dto.response.entity.VendedorReponse;
import com.example.SalesHub.exception.EntidadeDuplicadaException;
import com.example.SalesHub.exception.EntidadeNaoEncontradaException;
import com.example.SalesHub.mapper.VendedorMapper;
import com.example.SalesHub.model.Usuario;
import com.example.SalesHub.model.Vendedor;
import com.example.SalesHub.model.enums.Funcao;
import com.example.SalesHub.repository.customImpl.VendedorRepositoryImpl;
import com.example.SalesHub.repository.jpa.VendedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VendedorServiceTest {

    @Mock
    private VendedorRepository repository;
    @Mock
    private VendedorRepositoryImpl customRepository;
    @Mock
    private VendedorMapper mapper;
    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private VendedorService service;

    private VendedorRequest request;
    private Usuario usuario;
    private Vendedor vendedor;

    @BeforeEach
    void setup() {
        request = VendedorRequest.builder()
                .usuarioId(1L)
                .cpf("71430935002")
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .build();

        usuario = Usuario.builder().id(1L).funcao(Funcao.CLIENTE).build();
        vendedor = Vendedor.builder().id(1L).usuario(usuario).ativo(true).build();
    }

    @Test
    void deve_salvar_vendedor_com_sucesso() {
        var usuarioRes = UsuarioResponse.builder().id(1L).build();
        var vendedorRes = VendedorReponse.builder().id(1L).build();

        when(usuarioService.buscarUsuarioExistente(1L)).thenReturn(usuario);
        when(mapper.toEntity(any(), any())).thenReturn(vendedor);
        when(customRepository.buscarVendedorDuplicado(any())).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(vendedor);
        when(usuarioService.mapearUsuario(any())).thenReturn(usuarioRes);
        when(mapper.toResponse(any(), any())).thenReturn(vendedorRes);

        var resultado = service.salvar(request);

        assertThat(resultado).isNotNull();
        assertThat(usuario.getFuncao()).isEqualTo(Funcao.VENDEDOR);
        verify(repository).save(any());
    }

    @Test
    void deve_lancar_excecao_ao_salvar_vendedor_duplicado() {
        when(usuarioService.buscarUsuarioExistente(any())).thenReturn(usuario);
        when(mapper.toEntity(any(), any())).thenReturn(vendedor);
        when(customRepository.buscarVendedorDuplicado(any())).thenReturn(Optional.of(vendedor));

        assertThatThrownBy(() -> service.salvar(request))
                .isInstanceOf(EntidadeDuplicadaException.class)
                .hasMessage("Vendedor ja cadastrado");

        verify(repository, never()).save(any());
    }

    @Test
    void deve_reativar_vendedor_com_sucesso() {
        var reativacaoReq = VendedorReativacaoRequest.builder()
                .vendedorId(1L)
                .cpf("71430935002")
                .build();

        vendedor.setAtivo(false);
        when(customRepository.reativarContaVendedor(reativacaoReq)).thenReturn(Optional.of(vendedor));

        service.reativar(reativacaoReq);

        assertThat(vendedor.getAtivo()).isTrue();
        assertThat(usuario.getFuncao()).isEqualTo(Funcao.VENDEDOR);
    }

    @Test
    void deve_lancar_excecao_ao_reativar_vendedor_nao_encontrado() {
        var reativacaoReq = VendedorReativacaoRequest.builder().vendedorId(99L).build();
        when(customRepository.reativarContaVendedor(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.reativar(reativacaoReq))
                .isInstanceOf(EntidadeNaoEncontradaException.class);
    }

    @Test
    void deve_desativar_vendedor_com_sucesso() {
        when(customRepository.buscarVendedorExistente(1L)).thenReturn(Optional.of(vendedor));

        service.desativar(1L);

        assertThat(vendedor.getAtivo()).isFalse();
        assertThat(usuario.getFuncao()).isEqualTo(Funcao.CLIENTE);
    }

    @Test
    void deve_buscar_vendedores_paginados() {
        var filter = VendedorFilter.builder().build();
        var pageable = mock(Pageable.class);
        var page = new PageImpl<VendedorProjection>(List.of());

        when(customRepository.buscarVendedores(filter, pageable)).thenReturn(page);

        var resultado = service.buscar(filter, pageable);

        assertThat(resultado).isNotNull();
        verify(customRepository).buscarVendedores(filter, pageable);
    }
}
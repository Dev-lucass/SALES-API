package com.example.SalesHub.service;

import com.example.SalesHub.dto.filter.ProdutoFilter;
import com.example.SalesHub.dto.projection.ProdutoProjection;
import com.example.SalesHub.dto.request.ProdutoRequest;
import com.example.SalesHub.dto.response.entity.ProdutoResponse;
import com.example.SalesHub.exception.EntidadeDuplicadaException;
import com.example.SalesHub.exception.EntidadeNaoEncontradaException;
import com.example.SalesHub.mapper.ProdutoMapper;
import com.example.SalesHub.model.Produto;
import com.example.SalesHub.repository.customImpl.ProdutoRepositoryImpl;
import com.example.SalesHub.repository.jpa.ProdutoRepository;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository repository;

    @Mock
    private ProdutoMapper mapper;

    @Mock
    private ProdutoRepositoryImpl repositoryCustom;

    @InjectMocks
    private ProdutoService service;

    private Produto produto;
    private ProdutoRequest request;
    private ProdutoResponse response;

    @BeforeEach
    void setup() {
        request = ProdutoRequest.builder()
                .nome("Produto A")
                .descricao("Descricao A")
                .build();

        produto = Produto.builder()
                .id(1L)
                .nome("Produto A")
                .descricao("Descricao A")
                .ativo(true)
                .build();

        response = ProdutoResponse.builder()
                .id(1L)
                .nome("Produto A")
                .build();
    }

    @Test
    void deve_salvar_produto_com_sucesso() {
        when(mapper.toEntity(request)).thenReturn(produto);
        when(repositoryCustom.buscarProdutoDuplicado(produto)).thenReturn(Optional.empty());
        when(repository.save(produto)).thenReturn(produto);
        when(mapper.toResponse(produto)).thenReturn(response);

        var resultado = service.salvar(request);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
        verify(repository).save(produto);
    }

    @Test
    void deve_lancar_excecao_ao_salvar_produto_duplicado() {
        when(mapper.toEntity(request)).thenReturn(produto);
        when(repositoryCustom.buscarProdutoDuplicado(produto)).thenReturn(Optional.of(produto));

        assertThatThrownBy(() -> service.salvar(request))
                .isInstanceOf(EntidadeDuplicadaException.class)
                .hasMessage("Produto ja cadastrado");

        verify(repository, never()).save(any());
    }

    @Test
    void deve_buscar_produtos_paginado() {
        var filter = ProdutoFilter.builder().build();
        var pageable = mock(Pageable.class);
        var pagedResponse = new PageImpl<ProdutoProjection>(List.of());

        when(repositoryCustom.buscarProdutos(filter, pageable)).thenReturn(pagedResponse);

        var resultado = service.buscar(filter, pageable);

        assertThat(resultado).isNotNull();
        verify(repositoryCustom).buscarProdutos(filter, pageable);
    }

    @Test
    void deve_atualizar_produto_com_sucesso() {
        var novoRequest = ProdutoRequest.builder()
                .nome("Nome Editado")
                .descricao("Descricao Editada")
                .build();

        when(repositoryCustom.buscarProdutoExistente(1L)).thenReturn(Optional.of(produto));
        when(repositoryCustom.buscarProdutoDuplicado(produto)).thenReturn(Optional.empty());

        service.atualizar(1L, novoRequest);

        assertThat(produto.getNome()).isEqualTo("Nome Editado");
        assertThat(produto.getDescricao()).isEqualTo("Descricao Editada");
        verify(repository).save(produto);
    }

    @Test
    void deve_desativar_produto_com_sucesso() {
        when(repositoryCustom.buscarProdutoExistente(1L)).thenReturn(Optional.of(produto));

        service.desativar(1L);

        assertThat(produto.getAtivo()).isFalse();
        verify(repositoryCustom).buscarProdutoExistente(1L);
    }

    @Test
    void deve_lancar_excecao_ao_buscar_produto_inexistente() {
        when(repositoryCustom.buscarProdutoExistente(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarProdutoPeloId(99L))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessage("Produto não encontrado");
    }

    @Test
    void deve_mapear_produto_para_response_com_sucesso() {
        when(mapper.toResponse(produto)).thenReturn(response);

        var resultado = service.mapearProduto(produto);

        assertThat(resultado).isEqualTo(response);
        verify(mapper).toResponse(produto);
    }

    @Test
    void deve_buscar_produto_pelo_id_com_sucesso() {
        when(repositoryCustom.buscarProdutoExistente(1L)).thenReturn(Optional.of(produto));

        var resultado = service.buscarProdutoPeloId(1L);

        assertThat(resultado).isEqualTo(produto);
        verify(repositoryCustom).buscarProdutoExistente(1L);
    }
}
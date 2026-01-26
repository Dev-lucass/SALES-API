package com.example.SalesHub.service;

import com.example.SalesHub.dto.filter.ProdutoFilter;
import com.example.SalesHub.dto.projection.ProdutoProjection;
import com.example.SalesHub.dto.request.ProdutoRequest;
import com.example.SalesHub.dto.response.entity.ProdutoResponse;
import com.example.SalesHub.exception.EntidadeDuplicadaException;
import com.example.SalesHub.exception.EntidadeNaoEncontradaException;
import com.example.SalesHub.mapper.ProdutoMapper;
import com.example.SalesHub.model.Produto;
import com.example.SalesHub.repository.customImpl.ProdutoRepositoryCustom;
import com.example.SalesHub.repository.jpa.ProdutoRepository;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository repository;

    @Mock
    private ProdutoMapper mapper;

    @Mock
    private ProdutoRepositoryCustom repositoryCustom;

    @InjectMocks
    private ProdutoService service;

    @Test
    void deve_salvar_produto_com_sucesso() {
        var request = ProdutoRequest.builder()
                .nome("Mouse")
                .descricao("Mouse Gamer")
                .preco(BigDecimal.valueOf(100.00))
                .build();

        var produto = new Produto();
        var response = ProdutoResponse.builder().id(1L).nome("Mouse").build();

        Mockito.when(mapper.toEntity(request)).thenReturn(produto);
        Mockito.when(repositoryCustom.buscarProdutoDuplicado(produto)).thenReturn(Optional.empty());
        Mockito.when(repository.save(produto)).thenReturn(produto);
        Mockito.when(mapper.toResponse(produto)).thenReturn(response);

        var resultado = service.salvar(request);

        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado.id()).isEqualTo(1L);
        Mockito.verify(repository).save(produto);
    }

    @Test
    void deve_lancar_excecao_ao_salvar_produto_duplicado() {
        var request = ProdutoRequest.builder().nome("Mouse").build();
        var produto = new Produto();

        Mockito.when(mapper.toEntity(request)).thenReturn(produto);
        Mockito.when(repositoryCustom.buscarProdutoDuplicado(produto))
                .thenReturn(Optional.of(new Produto()));

        Assertions.assertThatThrownBy(() -> service.salvar(request))
                .isInstanceOf(EntidadeDuplicadaException.class)
                .hasMessage("Produto ja cadastrado");

        Mockito.verify(repository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void deve_buscar_produtos_paginado() {
        var filter = ProdutoFilter.builder().build();
        var pageable = Mockito.mock(Pageable.class);
        var pagedResponse = new PageImpl<ProdutoProjection>(List.of());

        Mockito.when(repositoryCustom.buscarProdutos(filter, pageable)).thenReturn(pagedResponse);

        var resultado = service.buscar(filter, pageable);

        Assertions.assertThat(resultado).isNotNull();
        Mockito.verify(repositoryCustom).buscarProdutos(filter, pageable);
    }

    @Test
    void deve_atualizar_produto_com_sucesso() {
        var id = 1L;
        var request = ProdutoRequest.builder()
                .nome("Novo Nome")
                .descricao("Nova Descrição")
                .preco(BigDecimal.valueOf(200.00))
                .build();

        var produtoExistente = new Produto();
        var response = ProdutoResponse.builder().id(id).nome("Novo Nome").build();

        Mockito.when(repositoryCustom.buscarProdutoExistente(id)).thenReturn(Optional.of(produtoExistente));
        Mockito.when(repositoryCustom.buscarProdutoDuplicado(produtoExistente)).thenReturn(Optional.empty());
        Mockito.when(repository.save(produtoExistente)).thenReturn(produtoExistente);
        Mockito.when(mapper.toResponse(produtoExistente)).thenReturn(response);

        var resultado = service.atualizar(id, request);

        Assertions.assertThat(resultado.nome()).isEqualTo("Novo Nome");
        Assertions.assertThat(produtoExistente.getNome()).isEqualTo("Novo Nome");
        Mockito.verify(repository).save(produtoExistente);
    }

    @Test
    void deve_lancar_excecao_ao_buscar_produto_inexistente() {
        var id = 1L;
        Mockito.when(repositoryCustom.buscarProdutoExistente(id)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.desativar(id))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessage("Produto não encontrado");
    }

    @Test
    void deve_desativar_produto_com_sucesso() {
        var id = 1L;
        var produto = new Produto();
        produto.setAtivo(true);

        Mockito.when(repositoryCustom.buscarProdutoExistente(id)).thenReturn(Optional.of(produto));

        service.desativar(id);

        Assertions.assertThat(produto.getAtivo()).isFalse();
    }
}
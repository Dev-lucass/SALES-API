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
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
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

    @Test
    void deve_salvar_produto_com_sucesso() {
        var request = ProdutoRequest.builder()
                .nome("Mouse")
                .descricao("Mouse Gamer")
                .preco(BigDecimal.valueOf(100.00))
                .build();

        var produto = Produto.builder().nome("Mouse").build();
        var response = ProdutoResponse.builder().id(1L).nome("Mouse").build();

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
        var request = ProdutoRequest.builder().nome("Mouse").build();
        var produto = Produto.builder().nome("Mouse").build();

        when(mapper.toEntity(request)).thenReturn(produto);
        when(repositoryCustom.buscarProdutoDuplicado(produto))
                .thenReturn(Optional.of(Produto.builder().build()));

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
        var id = 1L;
        var request = ProdutoRequest.builder()
                .nome("Novo Nome")
                .descricao("Nova Descrição")
                .preco(BigDecimal.valueOf(200.00))
                .build();

        var produtoExistente = Produto.builder().id(id).nome("Antigo").build();

        when(repositoryCustom.buscarProdutoExistente(id)).thenReturn(Optional.of(produtoExistente));
        when(repositoryCustom.buscarProdutoDuplicado(produtoExistente)).thenReturn(Optional.empty());

        service.atualizar(id, request);

        assertThat(produtoExistente.getNome()).isEqualTo("Novo Nome");
        assertThat(produtoExistente.getDescricao()).isEqualTo("Nova Descrição");
        verify(repository).save(produtoExistente);
    }

    @Test
    void deve_lancar_excecao_ao_buscar_produto_inexistente() {
        var id = 1L;
        when(repositoryCustom.buscarProdutoExistente(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.desativar(id))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessage("Produto não encontrado");
    }

    @Test
    void deve_desativar_produto_com_sucesso() {
        var id = 1L;
        var produto = Produto.builder().ativo(true).build();

        when(repositoryCustom.buscarProdutoExistente(id)).thenReturn(Optional.of(produto));

        service.desativar(id);

        assertThat(produto.getAtivo()).isFalse();
    }
}
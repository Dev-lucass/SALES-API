package com.example.SalesHub.service;

import com.example.SalesHub.dto.request.EstoqueRequest;
import com.example.SalesHub.dto.request.RetirarDoEstoqueRequest;
import com.example.SalesHub.dto.response.entity.EstoqueResponse;
import com.example.SalesHub.exception.EntidadeDuplicadaException;
import com.example.SalesHub.exception.EntidadeNaoEncontradaException;
import com.example.SalesHub.exception.QuantidadeIndiposnivelException;
import com.example.SalesHub.mapper.EstoqueMapper;
import com.example.SalesHub.model.Estoque;
import com.example.SalesHub.model.Produto;
import com.example.SalesHub.repository.customImpl.EstoqueRepositoryImpl;
import com.example.SalesHub.repository.jpa.EstoqueRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EstoqueServiceTest {

    @InjectMocks
    private EstoqueService service;

    @Mock
    private EstoqueRepositoryImpl repositoyCustom;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private EstoqueRepository repository;

    @Mock
    private EstoqueMapper mapper;

    @Test
    void deve_salvar_estoque_com_sucesso() {
        var request = EstoqueRequest.builder().produtoId(1L).quantidade(10L).build();
        var produto = Produto.builder().id(1L).build();
        var estoque = Estoque.builder().produto(produto).build();
        var response = EstoqueResponse.builder().id(1L).build();

        when(produtoService.buscarProdutoPeloId(1L)).thenReturn(produto);
        when(mapper.toEntity(request, produto)).thenReturn(estoque);
        when(repositoyCustom.buscarEstoqueDuplicado(estoque)).thenReturn(Optional.empty());
        when(repository.save(estoque)).thenReturn(estoque);
        when(mapper.toResponse(estoque)).thenReturn(response);

        var resultado = service.salvar(request);

        Assertions.assertThat(resultado).isNotNull();
        verify(repository).save(estoque);
    }

    @Test
    void deve_lancar_excecao_ao_salvar_estoque_duplicado() {
        var request = EstoqueRequest.builder().produtoId(1L).build();
        var produto = Produto.builder().id(1L).build();
        var estoque = Estoque.builder().produto(produto).build();

        when(produtoService.buscarProdutoPeloId(1L)).thenReturn(produto);
        when(mapper.toEntity(request, produto)).thenReturn(estoque);
        when(repositoyCustom.buscarEstoqueDuplicado(estoque)).thenReturn(Optional.of(estoque));

        Assertions.assertThatThrownBy(() -> service.salvar(request))
                .isInstanceOf(EntidadeDuplicadaException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void deve_desativar_estoque_com_sucesso() {
        var estoque = Estoque.builder().id(1L).ativo(true).build();
        when(repositoyCustom.buscarEstoqueExistente(1L)).thenReturn(Optional.of(estoque));

        service.desativar(1L);

        Assertions.assertThat(estoque.getAtivo()).isFalse();
    }

    @Test
    void deve_retirar_quantidade_do_estoque_com_sucesso() {
        var request = RetirarDoEstoqueRequest.builder().estoqueId(1L).quantidade(5L).build();
        var estoque = Estoque.builder().id(1L).quantidadeAtual(20L).build();
        var response = EstoqueResponse.builder().quantidadeAtual(15L).build();

        when(repositoyCustom.buscarEstoqueExistente(1L)).thenReturn(Optional.of(estoque));
        when(repository.save(estoque)).thenReturn(estoque);
        when(mapper.toResponse(estoque)).thenReturn(response);

        var resultado = service.pegarQuantidadeDoProdutoDoEstoque(request);

        Assertions.assertThat(estoque.getQuantidadeAtual()).isEqualTo(15L);
        Assertions.assertThat(resultado.quantidadeAtual()).isEqualTo(15L);
    }

    @Test
    void deve_lancar_excecao_quando_quantidade_for_insuficiente() {
        var request = RetirarDoEstoqueRequest.builder().estoqueId(1L).quantidade(100L).build();
        var estoque = Estoque.builder().id(1L).quantidadeAtual(50L).build();

        when(repositoyCustom.buscarEstoqueExistente(1L)).thenReturn(Optional.of(estoque));

        Assertions.assertThatThrownBy(() -> service.pegarQuantidadeDoProdutoDoEstoque(request))
                .isInstanceOf(QuantidadeIndiposnivelException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void deve_lancar_excecao_ao_buscar_estoque_inexistente() {
        when(repositoyCustom.buscarEstoqueExistente(1L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.desativar(1L))
                .isInstanceOf(EntidadeNaoEncontradaException.class);
    }
}
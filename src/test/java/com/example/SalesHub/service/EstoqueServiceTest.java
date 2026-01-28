package com.example.SalesHub.service;

import com.example.SalesHub.dto.request.EstoqueRequest;
import com.example.SalesHub.exception.EntidadeDuplicadaException;
import com.example.SalesHub.exception.EntidadeNaoEncontradaException;
import com.example.SalesHub.exception.QuantidadeIndiposnivelException;
import com.example.SalesHub.mapper.EstoqueMapper;
import com.example.SalesHub.model.Estoque;
import com.example.SalesHub.model.Produto;
import com.example.SalesHub.repository.customImpl.EstoqueRepositoryImpl;
import com.example.SalesHub.repository.jpa.EstoqueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EstoqueServiceTest {

    @InjectMocks
    private EstoqueService service;

    @Mock
    private EstoqueRepositoryImpl repositoryCustom;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private EstoqueRepository repository;

    @Mock
    private EstoqueMapper mapper;

    private EstoqueRequest request;
    private Produto produto;
    private Estoque estoque;

    @BeforeEach
    void setup() {
        request = EstoqueRequest.builder()
                .produtoId(1L)
                .quantidade(10L)
                .build();

        produto = Produto.builder()
                .id(1L)
                .nome("Produto Teste")
                .build();

        estoque = Estoque.builder()
                .id(1L)
                .produto(produto)
                .quantidadeAtual(10L)
                .ativo(true)
                .build();
    }

    @Test
    void deve_lancar_excecao_ao_salvar_produto_duplicado_no_estoque() {
        when(produtoService.buscarProdutoPeloId(any())).thenReturn(produto);
        when(mapper.toEntity(any(), any())).thenReturn(estoque);
        when(repositoryCustom.buscarEstoqueDuplicado(any())).thenReturn(Optional.of(estoque));

        assertThatThrownBy(() -> service.salvar(request))
                .isInstanceOf(EntidadeDuplicadaException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void deve_lancar_excecao_quando_estoque_nao_for_encontrado_ao_atualizar() {
        when(repositoryCustom.buscarEstoqueExistente(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.atualizar(1L, request))
                .isInstanceOf(EntidadeNaoEncontradaException.class);
    }

    @Test
    void deve_desativar_estoque_com_sucesso() {
        when(repositoryCustom.buscarEstoqueExistente(1L)).thenReturn(Optional.of(estoque));

        service.desativar(1L);

        verify(repositoryCustom).buscarEstoqueExistente(1L);
        assertThat(estoque.getAtivo()).isFalse();
    }

    @Test
    void deve_lancar_excecao_quando_quantidade_for_insuficiente() {
        estoque.setQuantidadeAtual(5L);
        when(repositoryCustom.buscarEstoqueExistente(1L)).thenReturn(Optional.of(estoque));

        assertThatThrownBy(() -> service.pegarQuantidadeDoProdutoDoEstoque(1L, 10L))
                .isInstanceOf(QuantidadeIndiposnivelException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void deve_lancar_excecao_quando_estoque_estiver_zerado() {
        estoque.setQuantidadeAtual(0L);
        when(repositoryCustom.buscarEstoqueExistente(1L)).thenReturn(Optional.of(estoque));

        assertThatThrownBy(() -> service.pegarQuantidadeDoProdutoDoEstoque(1L, 1L))
                .isInstanceOf(QuantidadeIndiposnivelException.class);
    }

    @Test
    void deve_subtrair_quantidade_e_salvar_com_sucesso() {
        when(repositoryCustom.buscarEstoqueExistente(1L)).thenReturn(Optional.of(estoque));
        when(repository.save(any())).thenReturn(estoque);

        service.pegarQuantidadeDoProdutoDoEstoque(1L, 4L);

        verify(repository).save(argThat(e -> e.getQuantidadeAtual() == 6L));
        verify(mapper).toResponse(any());
    }
}
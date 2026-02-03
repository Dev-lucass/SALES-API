package com.example.SalesHub.service;

import com.example.SalesHub.dto.filter.EstoqueFilter;
import com.example.SalesHub.dto.projection.EstoqueProjection;
import com.example.SalesHub.dto.request.EstoqueRequest;
import com.example.SalesHub.dto.response.entity.EstoqueResponse;
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
class EstoqueServiceTest {

    @Mock
    private EstoqueRepositoryImpl repositoryCustom;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private EstoqueRepository repository;

    @Mock
    private EstoqueMapper mapper;

    @InjectMocks
    private EstoqueService service;

    private Estoque estoque;
    private EstoqueRequest request;
    private EstoqueResponse response;
    private Produto produto;
    private EstoqueFilter filter;

    @BeforeEach
    void setup() {
        produto = Produto.builder()
                .id(10L)
                .nome("Produto Monitor")
                .descricao("Monitor 24 polegadas")
                .ativo(true)
                .build();

        request = EstoqueRequest.builder()
                .produtoId(10L)
                .quantidade(new BigDecimal("50.00"))
                .build();

        estoque = Estoque.builder()
                .id(1L)
                .produto(produto)
                .quantidadeAtual(new BigDecimal("50.00"))
                .ativo(true)
                .build();

        response = EstoqueResponse.builder()
                .id(1L)
                .quantidadeAtual(new BigDecimal("50.00"))
                .build();

        filter = EstoqueFilter.builder()
                .id(1L)
                .produtoId(10L)
                .quantidadeInicial(new BigDecimal("0.00"))
                .quantidadeAtual(new BigDecimal("50.00"))
                .build();
    }

    @Test
    void deve_salvar_estoque_com_sucesso() {
        when(produtoService.buscarProdutoPeloId(10L)).thenReturn(produto);
        when(mapper.toEntity(request, produto)).thenReturn(estoque);
        when(repositoryCustom.buscarEstoqueDuplicado(estoque)).thenReturn(Optional.empty());
        when(repository.save(estoque)).thenReturn(estoque);
        when(mapper.toResponse(estoque)).thenReturn(response);

        var resultado = service.salvar(request);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
        verify(repository).save(estoque);
    }

    @Test
    void deve_lancar_excecao_ao_salvar_estoque_duplicado() {
        when(produtoService.buscarProdutoPeloId(10L)).thenReturn(produto);
        when(mapper.toEntity(request, produto)).thenReturn(estoque);
        when(repositoryCustom.buscarEstoqueDuplicado(estoque)).thenReturn(Optional.of(estoque));

        assertThatThrownBy(() -> service.salvar(request))
                .isInstanceOf(EntidadeDuplicadaException.class)
                .hasMessageContaining("Produto ja cadastrado no estoque");

        verify(repository, never()).save(any());
    }

    @Test
    void deve_buscar_estoques_paginado() {
        var pageable = mock(Pageable.class);
        var pagedResponse = new PageImpl<EstoqueProjection>(List.of());

        when(repositoryCustom.buscarEstoques(filter, pageable)).thenReturn(pagedResponse);

        var resultado = service.buscar(filter, pageable);

        assertThat(resultado).isNotNull();
        verify(repositoryCustom).buscarEstoques(filter, pageable);
    }

    @Test
    void deve_atualizar_estoque_com_sucesso() {
        when(repositoryCustom.buscarEstoqueExistente(1L)).thenReturn(Optional.of(estoque));
        when(produtoService.buscarProdutoPeloId(10L)).thenReturn(produto);
        when(repositoryCustom.buscarEstoqueDuplicado(estoque)).thenReturn(Optional.empty());

        service.atualizar(1L, request);

        assertThat(estoque.getQuantidadeAtual()).isEqualByComparingTo(new BigDecimal("50.00"));
        verify(repository).save(estoque);
    }

    @Test
    void deve_desativar_estoque_com_sucesso() {
        when(repositoryCustom.buscarEstoqueExistente(1L)).thenReturn(Optional.of(estoque));

        service.desativar(1L);

        assertThat(estoque.getAtivo()).isFalse();
    }

    @Test
    void deve_retirar_quantidade_do_estoque_com_sucesso() {
        BigDecimal quantidadeParaRetirar = new BigDecimal("10.00");
        when(repositoryCustom.buscarEstoqueExistente(1L)).thenReturn(Optional.of(estoque));
        when(repository.save(estoque)).thenReturn(estoque);
        when(mapper.toResponse(estoque)).thenReturn(response);

        var resultado = service.pegarQuantidadeDoProdutoDoEstoque(1L, quantidadeParaRetirar);

        assertThat(resultado).isNotNull();
        assertThat(estoque.getQuantidadeAtual()).isEqualByComparingTo(new BigDecimal("40.00"));
        verify(repository).save(estoque);
    }

    @Test
    void deve_lancar_excecao_quando_estoque_insuficiente() {
        BigDecimal quantidadeIndisponivel = new BigDecimal("100.00");
        when(repositoryCustom.buscarEstoqueExistente(1L)).thenReturn(Optional.of(estoque));

        assertThatThrownBy(() -> service.pegarQuantidadeDoProdutoDoEstoque(1L, quantidadeIndisponivel))
                .isInstanceOf(QuantidadeIndiposnivelException.class)
                .hasMessage("Quantidade do produto indisponivel no estoque");

        verify(repository, never()).save(any());
    }

    @Test
    void deve_lancar_excecao_ao_buscar_estoque_por_id_inexistente() {
        when(repositoryCustom.buscarEstoqueExistente(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(99L))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessage("Estoque não encontrado");
    }
}
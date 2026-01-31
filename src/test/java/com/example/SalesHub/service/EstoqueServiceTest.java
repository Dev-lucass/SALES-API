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
    private Produto produto;
    private EstoqueRequest request;

    @BeforeEach
    void setup() {
        produto = Produto.builder().id(1L).nome("Produto Teste").build();
        estoque = Estoque.builder()
                .id(1L)
                .produto(produto)
                .quantidadeAtual(10L)
                .quantidadeInicial(10L)
                .ativo(true)
                .build();

        request = EstoqueRequest.builder()
                .produtoId(1L)
                .quantidade(5L)
                .build();
    }

    @Test
    void deve_salvar_estoque_com_sucesso() {
        when(produtoService.buscarProdutoPeloId(1L)).thenReturn(produto);
        when(mapper.toEntity(any(), any())).thenReturn(estoque);
        when(repositoryCustom.buscarEstoqueDuplicado(any())).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(estoque);
        when(mapper.toResponse(any())).thenReturn(EstoqueResponse.builder().id(1L).build());

        var resultado = service.salvar(request);

        assertThat(resultado).isNotNull();
        verify(repository).save(any());
    }

    @Test
    void deve_lancar_excecao_ao_salvar_estoque_duplicado() {
        when(produtoService.buscarProdutoPeloId(any())).thenReturn(produto);
        when(mapper.toEntity(any(), any())).thenReturn(estoque);
        when(repositoryCustom.buscarEstoqueDuplicado(any())).thenReturn(Optional.of(estoque));

        assertThatThrownBy(() -> service.salvar(request))
                .isInstanceOf(EntidadeDuplicadaException.class);
    }

    @Test
    void deve_pegar_quantidade_do_produto_com_sucesso_e_subtrair_do_total() {
        Long quantidadeParaRetirar = 4L;
        // Chamado duas vezes: uma na validação e outra na busca efetiva
        when(repositoryCustom.buscarEstoqueExistente(1L)).thenReturn(Optional.of(estoque));
        when(repository.save(any())).thenReturn(estoque);
        when(mapper.toResponse(any())).thenReturn(EstoqueResponse.builder().quantidadeAtual(6L).build());

        var resultado = service.pegarQuantidadeDoProdutoDoEstoque(1L, quantidadeParaRetirar);

        assertThat(estoque.getQuantidadeAtual()).isEqualTo(6L);
        assertThat(resultado.quantidadeAtual()).isEqualTo(6L);
        verify(repository).save(estoque);
    }

    @Test
    void deve_lancar_excecao_quando_quantidade_solicitada_for_maior_que_disponivel() {
        Long quantidadeExcessiva = 11L;
        when(repositoryCustom.buscarEstoqueExistente(1L)).thenReturn(Optional.of(estoque));

        assertThatThrownBy(() -> service.pegarQuantidadeDoProdutoDoEstoque(1L, quantidadeExcessiva))
                .isInstanceOf(QuantidadeIndiposnivelException.class)
                .hasMessage("Quantidade do produto indisponivel no estoque");
    }

    @Test
    void deve_desativar_estoque_com_sucesso() {
        when(repositoryCustom.buscarEstoqueExistente(1L)).thenReturn(Optional.of(estoque));

        service.desativar(1L);

        assertThat(estoque.getAtivo()).isFalse();
    }

    @Test
    void deve_buscar_estoques_paginados() {
        var filter = EstoqueFilter.builder().build();
        var pageable = mock(Pageable.class);
        var page = new PageImpl<EstoqueProjection>(List.of());

        when(repositoryCustom.buscarEstoques(filter, pageable)).thenReturn(page);

        var resultado = service.buscar(filter, pageable);

        assertThat(resultado).isNotNull();
        verify(repositoryCustom).buscarEstoques(filter, pageable);
    }

    @Test
    void deve_lancar_excecao_ao_buscar_estoque_inexistente() {
        when(repositoryCustom.buscarEstoqueExistente(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(99L))
                .isInstanceOf(EntidadeNaoEncontradaException.class);
    }
}
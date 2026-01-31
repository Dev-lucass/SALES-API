package com.example.SalesHub.service;

import com.example.SalesHub.dto.filter.VendaFilter;
import com.example.SalesHub.dto.projection.VendaProjection;
import com.example.SalesHub.dto.request.VendaRequest;
import com.example.SalesHub.dto.response.entity.EstoqueResponse;
import com.example.SalesHub.dto.response.entity.ProdutoResponse;
import com.example.SalesHub.dto.response.entity.UsuarioResponse;
import com.example.SalesHub.dto.response.entity.VendaResponse;
import com.example.SalesHub.mapper.VendaMapper;
import com.example.SalesHub.model.Estoque;
import com.example.SalesHub.model.Produto;
import com.example.SalesHub.model.Usuario;
import com.example.SalesHub.model.Venda;
import com.example.SalesHub.model.enums.Funcao;
import com.example.SalesHub.repository.customImpl.VendaRepositoryImpl;
import com.example.SalesHub.repository.jpa.VendaRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VendaServiceTest {

    @Mock
    private VendaMapper mapper;
    @Mock
    private VendaRepository repository;
    @Mock
    private VendaRepositoryImpl customRepository;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private ProdutoService produtoService;
    @Mock
    private EstoqueService estoqueService;
    @Mock
    private HistoricoService historicoService;

    @InjectMocks
    private VendaService service;

    private VendaRequest request;
    private Usuario usuario;
    private Produto produto;
    private Venda venda;

    @BeforeEach
    void setup() {
        request = VendaRequest.builder()
                .usuarioId(1L)
                .produtoId(1L)
                .estoqueId(1L)
                .quantidade(2L)
                .valor(new BigDecimal("100.00"))
                .desconto(10.0)
                .build();

        usuario = Usuario.builder()
                .id(1L)
                .funcao(Funcao.VENDEDOR) // Adicionado para passar na validação
                .build();

        produto = Produto.builder().id(1L).build();
        venda = spy(Venda.builder().id(1L).valor(new BigDecimal("90.00")).build());
    }

    @Test
    void deve_salvar_venda_com_sucesso_executando_todas_as_etapas() {
        var estoque = mock(Estoque.class);
        var estoqueRes = EstoqueResponse.builder().id(1L).build();
        var usuarioRes = UsuarioResponse.builder().id(1L).build();
        var produtoRes = ProdutoResponse.builder().id(1L).build();
        var vendaRes = VendaResponse.builder().id(1L).build();

        when(usuarioService.buscarUsuarioExistente(1L)).thenReturn(usuario);
        when(produtoService.buscarProdutoPeloId(1L)).thenReturn(produto);
        when(estoqueService.buscarPorId(1L)).thenReturn(estoque);
        when(estoqueService.pegarQuantidadeDoProdutoDoEstoque(1L, 2L)).thenReturn(estoqueRes);
        when(mapper.toEntity(request, usuario, produto)).thenReturn(venda);
        when(customRepository.buscarValorTotalDeVendas(any())).thenReturn(BigDecimal.ZERO);
        when(repository.save(venda)).thenReturn(venda);
        when(usuarioService.mapearUsuario(usuario)).thenReturn(usuarioRes);
        when(produtoService.mapearProduto(produto)).thenReturn(produtoRes);
        when(mapper.toReponse(venda, usuarioRes, produtoRes, estoqueRes)).thenReturn(vendaRes);

        var resultado = service.salvar(request);

        assertThat(resultado).isNotNull();
        verify(venda).aplicarDesconto(request.valor(), request.desconto());
        verify(historicoService).salvar(usuario, produto, estoque);
        verify(repository).save(venda);
    }

    @Test
    void deve_buscar_vendas_paginado() {
        var filter = VendaFilter.builder().build();
        var pageable = mock(Pageable.class);
        var pagedResponse = new PageImpl<VendaProjection>(List.of());

        when(customRepository.buscarVendas(filter, pageable)).thenReturn(pagedResponse);

        var resultado = service.buscar(filter, pageable);

        assertThat(resultado).isNotNull();
        verify(customRepository).buscarVendas(filter, pageable);
    }

    @Test
    void deve_garantir_que_o_fluxo_de_estoque_ocorra_antes_da_criacao_da_venda() {
        var estoque = mock(Estoque.class);
        var estoqueRes = EstoqueResponse.builder().build();

        when(usuarioService.buscarUsuarioExistente(any())).thenReturn(usuario);
        when(produtoService.buscarProdutoPeloId(any())).thenReturn(produto);
        when(estoqueService.buscarPorId(any())).thenReturn(estoque);
        when(estoqueService.pegarQuantidadeDoProdutoDoEstoque(any(), any())).thenReturn(estoqueRes);
        when(mapper.toEntity(any(), any(), any())).thenReturn(venda);

        service.salvar(request);

        var inOrder = inOrder(usuarioService, produtoService, estoqueService, mapper);
        inOrder.verify(usuarioService).buscarUsuarioExistente(any());
        inOrder.verify(produtoService).buscarProdutoPeloId(any());
        inOrder.verify(estoqueService).buscarPorId(any());
        inOrder.verify(estoqueService).pegarQuantidadeDoProdutoDoEstoque(any(), any());
        inOrder.verify(mapper).toEntity(any(), any(), any());
    }
}
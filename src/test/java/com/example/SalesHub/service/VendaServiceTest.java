package com.example.SalesHub.service;

import com.example.SalesHub.dto.filter.VendaFilter;
import com.example.SalesHub.dto.projection.VendaProjection;
import com.example.SalesHub.dto.request.VendaRequest;
import com.example.SalesHub.dto.response.entity.EstoqueResponse;
import com.example.SalesHub.dto.response.entity.ProdutoResponse;
import com.example.SalesHub.dto.response.entity.UsuarioResponse;
import com.example.SalesHub.dto.response.entity.VendaResponse;
import com.example.SalesHub.exception.FuncaoInvalidaException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    private Estoque estoque;
    private Venda venda;
    private VendaResponse response;
    private EstoqueResponse estoqueResponse;

    @BeforeEach
    void setup() {
        request = VendaRequest.builder()
                .usuarioId(1L)
                .produtoId(2L)
                .estoqueId(3L)
                .quantidade(new BigDecimal("2"))
                .valor(new BigDecimal("100"))
                .desconto(10.0)
                .build();

        usuario = Usuario.builder()
                .id(1L)
                .nome("Vendedor")
                .funcao(Funcao.VENDEDOR)
                .build();

        produto = Produto.builder()
                .id(2L)
                .nome("Produto")
                .build();

        estoque = Estoque.builder()
                .id(3L)
                .build();

        venda = Venda.builder()
                .id(1L)
                .usuario(usuario)
                .produto(produto)
                .valor(new BigDecimal("90"))
                .quantidade(new BigDecimal("2"))
                .build();

        estoqueResponse = EstoqueResponse.builder()
                .id(3L)
                .quantidadeAtual(new BigDecimal("10"))
                .build();

        response = VendaResponse.builder()
                .id(1L)
                .valor(new BigDecimal("90"))
                .valorTotalVendas(new BigDecimal("1090"))
                .build();
    }

    @Test
    void deve_salvar_venda_com_sucesso() {
        when(usuarioService.buscarUsuarioExistente(1L)).thenReturn(usuario);
        when(produtoService.buscarProdutoPeloId(2L)).thenReturn(produto);
        when(estoqueService.buscarPorId(3L)).thenReturn(estoque);
        when(estoqueService.pegarQuantidadeDoProdutoDoEstoque(3L, request.quantidade())).thenReturn(estoqueResponse);
        when(mapper.toEntity(request, usuario, produto)).thenReturn(venda);
        when(customRepository.buscarValorTotalDeVendas(venda)).thenReturn(new BigDecimal("1000"));
        when(repository.save(venda)).thenReturn(venda);
        when(usuarioService.mapearUsuario(usuario)).thenReturn(mock(UsuarioResponse.class));
        when(produtoService.mapearProduto(produto)).thenReturn(mock(ProdutoResponse.class));
        when(mapper.toReponse(any(), any(), any(), any())).thenReturn(response);

        var resultado = service.salvar(request);

        assertThat(resultado).isNotNull();
        verify(historicoService).salvar(usuario, produto, estoque, request.quantidade());
        verify(repository).save(venda);
        assertThat(venda.getValorTotalVendas()).isEqualByComparingTo("1090");
    }

    @Test
    void deve_lancar_excecao_quando_usuario_nao_for_vendedor() {
        usuario.setFuncao(Funcao.CLIENTE);
        when(usuarioService.buscarUsuarioExistente(1L)).thenReturn(usuario);

        assertThatThrownBy(() -> service.salvar(request))
                .isInstanceOf(FuncaoInvalidaException.class)
                .hasMessage("Você não é um vendedor, preencha o formulario para se tornar um");

        verifyNoInteractions(repository);
        verifyNoInteractions(historicoService);
    }

    @Test
    void deve_buscar_vendas_paginado() {
        var filter = VendaFilter.builder().id(1L).build();
        var pageable = mock(Pageable.class);
        var pagedResponse = new PageImpl<VendaProjection>(List.of());

        when(customRepository.buscarVendas(filter, pageable)).thenReturn(pagedResponse);

        var resultado = service.buscar(filter, pageable);

        assertThat(resultado).isNotNull();
        verify(customRepository).buscarVendas(filter, pageable);
    }
}
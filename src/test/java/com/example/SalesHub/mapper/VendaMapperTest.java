package com.example.SalesHub.mapper;

import com.example.SalesHub.dto.request.VendaRequest;
import com.example.SalesHub.dto.response.entity.EstoqueResponse;
import com.example.SalesHub.dto.response.entity.ProdutoResponse;
import com.example.SalesHub.dto.response.entity.UsuarioResponse;
import com.example.SalesHub.model.Produto;
import com.example.SalesHub.model.Usuario;
import com.example.SalesHub.model.Venda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VendaMapperTest {

    private VendaMapper mapper;
    private Usuario usuario;
    private Produto produto;
    private VendaRequest request;
    private Venda venda;
    private UsuarioResponse usuarioResponse;
    private ProdutoResponse produtoResponse;
    private EstoqueResponse estoqueResponse;

    @BeforeEach
    void setup() {
        mapper = new VendaMapper();

        usuario = Usuario.builder()
                .id(1L)
                .nome("Vendedor Teste")
                .build();

        produto = Produto.builder()
                .id(2L)
                .nome("Produto Teste")
                .build();

        request = VendaRequest.builder()
                .usuarioId(1L)
                .produtoId(2L)
                .estoqueId(3L)
                .quantidade(new BigDecimal("2.00"))
                .valor(new BigDecimal("100.00"))
                .desconto(10.0)
                .build();

        venda = Venda.builder()
                .id(500L)
                .usuario(usuario)
                .produto(produto)
                .valor(new BigDecimal("90.00"))
                .quantidade(new BigDecimal("2.00"))
                .valorTotalVendas(new BigDecimal("1090.00"))
                .dataVenda(LocalDateTime.now())
                .build();

        usuarioResponse = UsuarioResponse.builder().id(1L).nome("Vendedor Teste").build();
        produtoResponse = ProdutoResponse.builder().id(2L).nome("Produto Teste").build();
        estoqueResponse = EstoqueResponse.builder().id(3L).build();
    }

    @Test
    void deve_converter_venda_request_para_entidade() {
        var resultado = mapper.toEntity(request, usuario, produto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getUsuario()).isEqualTo(usuario);
        assertThat(resultado.getProduto()).isEqualTo(produto);
        assertThat(resultado.getValor()).isEqualByComparingTo(request.valor());
        assertThat(resultado.getQuantidade()).isEqualByComparingTo(request.quantidade());
        assertThat(resultado.getDataVenda()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void deve_converter_entidade_venda_para_response() {
        var resultado = mapper.toReponse(venda, usuarioResponse, produtoResponse, estoqueResponse);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(venda.getId());
        assertThat(resultado.usuario()).isEqualTo(usuarioResponse);
        assertThat(resultado.produto()).isEqualTo(produtoResponse);
        assertThat(resultado.estoque()).isEqualTo(estoqueResponse);
        assertThat(resultado.valor()).isEqualByComparingTo(venda.getValor());
        assertThat(resultado.quantidade()).isEqualByComparingTo(venda.getQuantidade());
        assertThat(resultado.valorTotalVendas()).isEqualByComparingTo(venda.getValorTotalVendas());
        assertThat(resultado.dataVenda()).isEqualTo(venda.getDataVenda());
    }
}
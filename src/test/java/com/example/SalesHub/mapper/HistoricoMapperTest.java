package com.example.SalesHub.mapper;

import com.example.SalesHub.dto.response.entity.EstoqueResponse;
import com.example.SalesHub.dto.response.entity.ProdutoResponse;
import com.example.SalesHub.dto.response.entity.UsuarioResponse;
import com.example.SalesHub.model.Estoque;
import com.example.SalesHub.model.Historico;
import com.example.SalesHub.model.Produto;
import com.example.SalesHub.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HistoricoMapperTest {

    private HistoricoMapper mapper;
    private Usuario usuario;
    private Produto produto;
    private Estoque estoque;
    private Historico historico;
    private UsuarioResponse usuarioResponse;
    private ProdutoResponse produtoResponse;
    private EstoqueResponse estoqueResponse;

    @BeforeEach
    void setup() {
        mapper = new HistoricoMapper();

        usuario = Usuario.builder()
                .id(1L)
                .nome("Usuario Teste")
                .build();

        produto = Produto.builder()
                .id(2L)
                .nome("Produto Teste")
                .build();

        estoque = Estoque.builder()
                .id(3L)
                .build();

        historico = Historico.builder()
                .id(100L)
                .usuario(usuario)
                .produto(produto)
                .estoque(estoque)
                .quantidadeRetirada(new BigDecimal("10.00"))
                .criadoEm(LocalDateTime.now())
                .build();

        usuarioResponse = UsuarioResponse.builder()
                .id(1L)
                .nome("Usuario Teste")
                .build();

        produtoResponse = ProdutoResponse.builder()
                .id(2L)
                .nome("Produto Teste")
                .build();

        estoqueResponse = EstoqueResponse.builder()
                .id(3L)
                .build();
    }

    @Test
    void deve_converter_para_entidade_historico() {
        var quantidade = new BigDecimal("5.00");

        var resultado = mapper.toEntity(usuario, produto, estoque, quantidade);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getUsuario()).isEqualTo(usuario);
        assertThat(resultado.getProduto()).isEqualTo(produto);
        assertThat(resultado.getEstoque()).isEqualTo(estoque);
        assertThat(resultado.getQuantidadeRetirada()).isEqualByComparingTo(quantidade);
        assertThat(resultado.getCriadoEm()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void deve_converter_entidade_para_response_historico() {
        var resultado = mapper.toResponse(historico, usuarioResponse, produtoResponse, estoqueResponse);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(historico.getId());
        assertThat(resultado.usuario()).isEqualTo(usuarioResponse);
        assertThat(resultado.produto()).isEqualTo(produtoResponse);
        assertThat(resultado.estoque()).isEqualTo(estoqueResponse);
        assertThat(resultado.criadoEm()).isEqualTo(historico.getCriadoEm());
    }
}
package com.example.SalesHub.mapper;

import com.example.SalesHub.dto.response.entity.EstoqueResponse;
import com.example.SalesHub.dto.response.entity.ProdutoResponse;
import com.example.SalesHub.dto.response.entity.UsuarioResponse;
import com.example.SalesHub.model.Estoque;
import com.example.SalesHub.model.Historico;
import com.example.SalesHub.model.Produto;
import com.example.SalesHub.model.Usuario;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HistoricoMapperTest {

    private final HistoricoMapper mapper = new HistoricoMapper();

    @Test
    void deve_mapear_entidade_corretamente() {

        var usuario = Usuario.builder().id(1L).nome("Usuario Teste").build();
        var produto = Produto.builder().id(10L).nome("Produto Teste").build();
        var estoque = Estoque.builder().id(100L).quantidadeInicial(5L).build();
        var quantidadeRetirada = 0L;

        var resultado = mapper.toEntity(usuario, produto, estoque, quantidadeRetirada);

        assertAll(
                () -> assertNotNull(resultado),
                () -> assertEquals(usuario, resultado.getUsuario()),
                () -> assertEquals(produto, resultado.getProduto()),
                () -> assertEquals(estoque, resultado.getEstoque()),
                () -> assertEquals(quantidadeRetirada, resultado.getQuantidadeRetirada()),
                () -> assertNotNull(resultado.getCriadoEm()),
                () -> assertTrue(resultado.getCriadoEm().isBefore(LocalDateTime.now().plusSeconds(1)))
        );
    }

    @Test
    void deve_mapear_response_corretamente() {
        var criadoEm = LocalDateTime.of(2026, 1, 31, 10, 0, 0);
        var historico = Historico.builder()
                .id(500L)
                .criadoEm(criadoEm)
                .build();

        var usuarioResponse = UsuarioResponse.builder().id(1L).build();
        var produtoResponse = ProdutoResponse.builder().id(10L).build();
        var estoqueResponse = EstoqueResponse.builder().id(100L).build();

        var resultado = mapper.toResponse(historico, usuarioResponse, produtoResponse, estoqueResponse);

        assertAll(
                () -> assertNotNull(resultado),
                () -> assertEquals(500L, resultado.id()),
                () -> assertEquals(usuarioResponse, resultado.usuario()),
                () -> assertEquals(produtoResponse, resultado.produto()),
                () -> assertEquals(estoqueResponse, resultado.estoque()),
                () -> assertEquals(criadoEm, resultado.criadoEm())
        );
    }

    @Test
    void deve_lidar_com_campos_nulos_no_mapeamento_de_entidade() {
        var resultado = mapper.toEntity(null, null, null, null);

        assertAll(
                () -> assertNotNull(resultado),
                () -> assertNull(resultado.getUsuario()),
                () -> assertNull(resultado.getProduto()),
                () -> assertNull(resultado.getEstoque()),
                () -> assertNotNull(resultado.getCriadoEm())
        );
    }

    @Test
    void deve_lidar_com_campos_nulos_no_mapeamento_de_response() {
        var historico = Historico.builder().id(1L).build();

        var resultado = mapper.toResponse(historico, null, null, null);

        assertAll(
                () -> assertEquals(1L, resultado.id()),
                () -> assertNull(resultado.usuario()),
                () -> assertNull(resultado.produto()),
                () -> assertNull(resultado.estoque()),
                () -> assertNull(resultado.criadoEm())
        );
    }
}
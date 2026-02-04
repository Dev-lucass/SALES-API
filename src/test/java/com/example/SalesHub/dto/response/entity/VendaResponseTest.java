package com.example.SalesHub.dto.response.entity;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VendaResponseTest {

    @Test
    void deve_criar_venda_response_com_sucesso_usando_builder() {
        var usuario = UsuarioResponse.builder()
                .id(1L)
                .nome("Vendedor Teste")
                .email("vendedor@saleshub.com")
                .build();

        var produto = ProdutoResponse.builder()
                .id(10L)
                .nome("Notebook")
                .descricao("I7 16GB")
                .build();

        var estoque = EstoqueResponse.builder()
                .id(100L)
                .produtoId(10L)
                .quantidadeInicial(new BigDecimal("50.00"))
                .quantidadeAtual(new BigDecimal("40.00"))
                .build();

        var dataVenda = LocalDateTime.of(2026, 2, 3, 14, 0, 0);
        var valor = new BigDecimal("4500.00");
        var quantidade = new BigDecimal("2.00");
        var valorTotal = new BigDecimal("9000.00");

        var response = VendaResponse.builder()
                .id(1000L)
                .usuario(usuario)
                .produto(produto)
                .estoque(estoque)
                .valor(valor)
                .quantidade(quantidade)
                .valorTotalVendas(valorTotal)
                .dataVenda(dataVenda)
                .build();

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1000L);
        assertThat(response.usuario()).isEqualTo(usuario);
        assertThat(response.produto()).isEqualTo(produto);
        assertThat(response.estoque()).isEqualTo(estoque);
        assertThat(response.valor()).isEqualByComparingTo(valor);
        assertThat(response.quantidade()).isEqualByComparingTo(quantidade);
        assertThat(response.valorTotalVendas()).isEqualByComparingTo(valorTotal);
        assertThat(response.dataVenda()).isEqualTo(dataVenda);
    }

    @Test
    void deve_garantir_que_nenhum_campo_da_venda_response_seja_nulo() {
        var usuario = UsuarioResponse.builder().id(1L).nome("U").email("e").build();
        var produto = ProdutoResponse.builder().id(1L).nome("P").descricao("D").build();
        var estoque = EstoqueResponse.builder().id(1L).produtoId(1L).quantidadeInicial(BigDecimal.ONE).quantidadeAtual(BigDecimal.ONE).build();

        var response = VendaResponse.builder()
                .id(1L)
                .usuario(usuario)
                .produto(produto)
                .estoque(estoque)
                .valor(BigDecimal.TEN)
                .quantidade(BigDecimal.ONE)
                .valorTotalVendas(BigDecimal.TEN)
                .dataVenda(LocalDateTime.now())
                .build();

        assertThat(response.id()).isNotNull();
        assertThat(response.usuario()).isNotNull();
        assertThat(response.produto()).isNotNull();
        assertThat(response.estoque()).isNotNull();
        assertThat(response.valor()).isNotNull();
        assertThat(response.quantidade()).isNotNull();
        assertThat(response.valorTotalVendas()).isNotNull();
        assertThat(response.dataVenda()).isNotNull();
    }

    @Test
    void deve_testar_igualdade_e_hashcode_do_record_venda_response() {
        var agora = LocalDateTime.now();
        
        var v1 = VendaResponse.builder()
                .id(1L)
                .valor(BigDecimal.TEN)
                .dataVenda(agora)
                .build();

        var v2 = VendaResponse.builder()
                .id(1L)
                .valor(BigDecimal.TEN)
                .dataVenda(agora)
                .build();

        assertThat(v1).isEqualTo(v2);
        assertThat(v1.hashCode()).isEqualTo(v2.hashCode());
    }
}
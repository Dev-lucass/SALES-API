package com.example.SalesHub.dto.response.entity;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HistoricoResponseTest {

    @Test
    void deve_criar_historico_response_com_sucesso_usando_builder_e_objetos_aninhados() {
        var usuario = UsuarioResponse.builder()
                .id(1L)
                .nome("Admin")
                .email("admin@saleshub.com")
                .build();

        var produto = ProdutoResponse.builder()
                .id(10L)
                .nome("Cadeira Gamer")
                .descricao("Cadeira ergonômica")
                .build();

        var estoque = EstoqueResponse.builder()
                .id(100L)
                .produtoId(10L)
                .quantidadeInicial(new BigDecimal("10.00"))
                .quantidadeAtual(new BigDecimal("5.00"))
                .build();

        var agora = LocalDateTime.now();
      
        var response = HistoricoResponse.builder()
                .id(500L)
                .usuario(usuario)
                .produto(produto)
                .estoque(estoque)
                .criadoEm(agora)
                .build();

    
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(500L);
        assertThat(response.usuario()).isEqualTo(usuario);
        assertThat(response.produto()).isEqualTo(produto);
        assertThat(response.estoque()).isEqualTo(estoque);
        assertThat(response.criadoEm()).isEqualTo(agora);
       
        assertThat(response.usuario().nome()).isNotNull();
        assertThat(response.produto().nome()).isNotNull();
        assertThat(response.estoque().quantidadeAtual()).isNotNull();
    }

    @Test
    void deve_testar_igualdade_e_hashcode_do_record_historico_response() {
        var agora = LocalDateTime.now();
        
        var h1 = HistoricoResponse.builder()
                .id(1L)
                .criadoEm(agora)
                .build();

        var h2 = HistoricoResponse.builder()
                .id(1L)
                .criadoEm(agora)
                .build();

        assertThat(h1).isEqualTo(h2);
        assertThat(h1.hashCode()).isEqualTo(h2.hashCode());
    }
}
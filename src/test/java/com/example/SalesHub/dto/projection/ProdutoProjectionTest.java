package com.example.SalesHub.dto.projection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProdutoProjectionTest {

    @Test
    void deve_criar_produtoProjection_completa() {
        var agora = LocalDateTime.now();
        var produtoProjection = ProdutoProjection.builder()
                .id(10L)
                .nome("Cadeira Gamer")
                .descricao("Cadeira ergonômica preta")
                .preco(BigDecimal.valueOf(1200.50))
                .criadoEm(agora)
                .build();

        Assertions.assertThat(produtoProjection)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.id()).isEqualTo(10L);
                    Assertions.assertThat(saida.nome()).isEqualTo("Cadeira Gamer");
                    Assertions.assertThat(saida.descricao()).isEqualTo("Cadeira ergonômica preta");
                    Assertions.assertThat(saida.preco())
                            .usingComparator(BigDecimal::compareTo)
                            .isEqualTo(BigDecimal.valueOf(1200.50));
                    Assertions.assertThat(saida.criadoEm()).isEqualTo(agora);
                });
    }

    @Test
    void deve_criar_produtoProjection_com_campos_nulos() {
        var produtoProjection = ProdutoProjection.builder()
                .id(null)
                .nome(null)
                .descricao(null)
                .preco(null)
                .criadoEm(null)
                .build();

        Assertions.assertThat(produtoProjection)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.id()).isNull();
                    Assertions.assertThat(saida.nome()).isNull();
                    Assertions.assertThat(saida.descricao()).isNull();
                    Assertions.assertThat(saida.preco()).isNull();
                    Assertions.assertThat(saida.criadoEm()).isNull();
                });
    }

    @Test
    void deve_garantir_igualdade_de_preco_com_escalas_diferentes() {
        var produtoProjection = ProdutoProjection.builder()
                .preco(BigDecimal.valueOf(500.0))
                .build();

        Assertions.assertThat(produtoProjection.preco())
                .usingComparator(BigDecimal::compareTo)
                .isEqualTo(BigDecimal.valueOf(500.0000));
    }
}
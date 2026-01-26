package com.example.SalesHub.dto.filter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProdutoFilterTest {

    @Test
    void deve_criar_produtoFilter_completo() {
        var dataInicial = LocalDate.of(2023, 1, 1);
        var dataFinal = LocalDate.of(2023, 12, 31);

        var produtoFilter = ProdutoFilter.builder()
                .id(1L)
                .nome("Teclado")
                .descricao("Mecânico")
                .preco(BigDecimal.valueOf(250.00))
                .dataInicial(dataInicial)
                .dataFinal(dataFinal)
                .build();

        Assertions.assertThat(produtoFilter)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.id()).isEqualTo(1L);
                    Assertions.assertThat(saida.nome()).isEqualTo("Teclado");
                    Assertions.assertThat(saida.descricao()).isEqualTo("Mecânico");
                    Assertions.assertThat(saida.preco())
                            .usingComparator(BigDecimal::compareTo)
                            .isEqualTo(BigDecimal.valueOf(250.00));
                    Assertions.assertThat(saida.dataInicial()).isEqualTo(dataInicial);
                    Assertions.assertThat(saida.dataFinal()).isEqualTo(dataFinal);
                });
    }

    @Test
    void deve_criar_produtoFilter_com_campos_nulos() {
        var produtoFilter = ProdutoFilter.builder()
                .id(null)
                .nome(null)
                .descricao(null)
                .preco(null)
                .dataInicial(null)
                .dataFinal(null)
                .build();

        Assertions.assertThat(produtoFilter)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.id()).isNull();
                    Assertions.assertThat(saida.nome()).isNull();
                    Assertions.assertThat(saida.descricao()).isNull();
                    Assertions.assertThat(saida.preco()).isNull();
                    Assertions.assertThat(saida.dataInicial()).isNull();
                    Assertions.assertThat(saida.dataFinal()).isNull();
                });
    }

    @Test
    void deve_garantir_igualdade_de_preco_com_escalas_diferentes() {
        var produtoFilter = ProdutoFilter.builder()
                .preco(BigDecimal.valueOf(100.0))
                .build();

        Assertions.assertThat(produtoFilter.preco())
                .usingComparator(BigDecimal::compareTo)
                .isEqualTo(new BigDecimal("100.000"));
    }
}
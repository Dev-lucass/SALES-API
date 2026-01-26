package com.example.SalesHub.dto.response.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProdutoResponseTest {

    @Test
    void deve_criar_produtoResponse_completo() {
        var agora = LocalDateTime.of(2023, 5, 20, 14, 30, 15);
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        var produtoResponse = ProdutoResponse.builder()
                .id(1L)
                .nome("Monitor")
                .descricao("Monitor 4k")
                .preco(BigDecimal.valueOf(1500.00))
                .criadoEm(agora)
                .build();

        Assertions.assertThat(produtoResponse)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.id()).isEqualTo(1L);
                    Assertions.assertThat(saida.nome()).isEqualTo("Monitor");
                    Assertions.assertThat(saida.descricao()).isEqualTo("Monitor 4k");
                    Assertions.assertThat(saida.preco())
                            .usingComparator(BigDecimal::compareTo)
                            .isEqualTo(BigDecimal.valueOf(1500.00));
                    Assertions.assertThat(saida.criadoEm().format(formatter))
                            .isEqualTo("20/05/2023 14:30:15");
                });
    }

    @Test
    void deve_criar_produtoResponse_nulo() {
        var produtoResponse = ProdutoResponse.builder()
                .id(null)
                .nome(null)
                .descricao(null)
                .preco(null)
                .criadoEm(null)
                .build();

        Assertions.assertThat(produtoResponse)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.id()).isNull();
                    Assertions.assertThat(saida.nome()).isNull();
                    Assertions.assertThat(saida.descricao()).isNull();
                    Assertions.assertThat(saida.preco()).isNull();
                    Assertions.assertThat(saida.criadoEm()).isNull();
                });
    }
}
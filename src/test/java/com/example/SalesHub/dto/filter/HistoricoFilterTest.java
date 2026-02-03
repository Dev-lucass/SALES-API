package com.example.SalesHub.dto.filter;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HistoricoFilterTest {

    @Test
    void deve_criar_historico_filter_com_sucesso_usando_builder() {
        var id = 1L;
        var usuarioId = 10L;
        var produtoId = 20L;
        var estoqueId = 30L;
        var quantidade = new BigDecimal("15.50");
        var dataInicial = LocalDate.of(2023, 1, 1);
        var dataFinal = LocalDate.of(2023, 12, 31);

        var filter = HistoricoFilter.builder()
                .id(id)
                .usuarioId(usuarioId)
                .produtoId(produtoId)
                .estoqueId(estoqueId)
                .quantidadeRetirada(quantidade)
                .dataInicial(dataInicial)
                .dataFinal(dataFinal)
                .build();

        assertThat(filter).isNotNull();
        assertThat(filter.id()).isEqualTo(id);
        assertThat(filter.usuarioId()).isEqualTo(usuarioId);
        assertThat(filter.produtoId()).isEqualTo(produtoId);
        assertThat(filter.estoqueId()).isEqualTo(estoqueId);
        assertThat(filter.quantidadeRetirada()).isEqualByComparingTo(quantidade);
        assertThat(filter.dataInicial()).isEqualTo(dataInicial);
        assertThat(filter.dataFinal()).isEqualTo(dataFinal);
    }

    @Test
    void deve_garantir_que_todos_os_campos_do_filtro_nao_sao_nulos() {
        var filter = HistoricoFilter.builder()
                .id(1L)
                .usuarioId(2L)
                .produtoId(3L)
                .estoqueId(4L)
                .quantidadeRetirada(BigDecimal.ONE)
                .dataInicial(LocalDate.now())
                .dataFinal(LocalDate.now())
                .build();

        assertThat(filter.id()).isNotNull();
        assertThat(filter.usuarioId()).isNotNull();
        assertThat(filter.produtoId()).isNotNull();
        assertThat(filter.estoqueId()).isNotNull();
        assertThat(filter.quantidadeRetirada()).isNotNull();
        assertThat(filter.dataInicial()).isNotNull();
        assertThat(filter.dataFinal()).isNotNull();
    }

    @Test
    void deve_testar_igualdade_do_record_historico_filter() {
        var filter1 = HistoricoFilter.builder()
                .id(1L)
                .quantidadeRetirada(BigDecimal.TEN)
                .build();

        var filter2 = HistoricoFilter.builder()
                .id(1L)
                .quantidadeRetirada(BigDecimal.TEN)
                .build();

        assertThat(filter1).isEqualTo(filter2);
        assertThat(filter1.hashCode()).isEqualTo(filter2.hashCode());
    }
}
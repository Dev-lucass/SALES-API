package com.example.SalesHub.dto.filter;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VendaFilterTest {

    @Test
    void deve_criar_venda_filter_com_sucesso_usando_builder() {
        var id = 500L;
        var usuarioId = 1L;
        var valor = new BigDecimal("250.00");
        var quantidade = new BigDecimal("10.00");
        var valorTotal = new BigDecimal("2500.00");
        var dataInicial = LocalDate.of(2026, 1, 1);
        var dataFinal = LocalDate.of(2026, 2, 3);

        var filter = VendaFilter.builder()
                .id(id)
                .usuarioId(usuarioId)
                .valor(valor)
                .quantidade(quantidade)
                .valorTotalVendas(valorTotal)
                .dataInicial(dataInicial)
                .dataFinal(dataFinal)
                .build();

        assertThat(filter).isNotNull();
        assertThat(filter.id()).isEqualTo(id);
        assertThat(filter.usuarioId()).isEqualTo(usuarioId);
        assertThat(filter.valor()).isEqualByComparingTo(valor);
        assertThat(filter.quantidade()).isEqualByComparingTo(quantidade);
        assertThat(filter.valorTotalVendas()).isEqualByComparingTo(valorTotal);
        assertThat(filter.dataInicial()).isEqualTo(dataInicial);
        assertThat(filter.dataFinal()).isEqualTo(dataFinal);
    }

    @Test
    void deve_garantir_que_todos_os_campos_do_venda_filter_nao_sao_nulos() {
        var filter = VendaFilter.builder()
                .id(1L)
                .usuarioId(1L)
                .valor(BigDecimal.TEN)
                .quantidade(BigDecimal.ONE)
                .valorTotalVendas(BigDecimal.TEN)
                .dataInicial(LocalDate.now())
                .dataFinal(LocalDate.now())
                .build();

        assertThat(filter.id()).isNotNull();
        assertThat(filter.usuarioId()).isNotNull();
        assertThat(filter.valor()).isNotNull();
        assertThat(filter.quantidade()).isNotNull();
        assertThat(filter.valorTotalVendas()).isNotNull();
        assertThat(filter.dataInicial()).isNotNull();
        assertThat(filter.dataFinal()).isNotNull();
    }

    @Test
    void deve_testar_igualdade_do_record_venda_filter() {
        var hoje = LocalDate.now();

        var filter1 = VendaFilter.builder()
                .id(100L)
                .dataInicial(hoje)
                .build();

        var filter2 = VendaFilter.builder()
                .id(100L)
                .dataInicial(hoje)
                .build();

        assertThat(filter1).isEqualTo(filter2);
        assertThat(filter1.hashCode()).isEqualTo(filter2.hashCode());
    }
}
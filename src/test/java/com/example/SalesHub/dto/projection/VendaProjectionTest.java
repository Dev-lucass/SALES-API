package com.example.SalesHub.dto.projection;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VendaProjectionTest {

    @Test
    void deve_criar_venda_projection_com_sucesso_usando_builder() {
        var id = 100L;
        var usuarioId = 1L;
        var nomeUsuario = "Junior Testador";
        var valor = new BigDecimal("150.50");
        var quantidade = new BigDecimal("5.00");
        var valorTotal = new BigDecimal("752.50");
        var dataVenda = LocalDateTime.of(2026, 2, 3, 12, 0, 0);

        var projection = VendaProjection.builder()
                .id(id)
                .usuarioId(usuarioId)
                .usuario(nomeUsuario)
                .valor(valor)
                .quantidade(quantidade)
                .valorTotalVendas(valorTotal)
                .dataVenda(dataVenda)
                .build();

        assertThat(projection).isNotNull();
        assertThat(projection.id()).isEqualTo(id);
        assertThat(projection.usuarioId()).isEqualTo(usuarioId);
        assertThat(projection.usuario()).isEqualTo(nomeUsuario);
        assertThat(projection.valor()).isEqualByComparingTo(valor);
        assertThat(projection.quantidade()).isEqualByComparingTo(quantidade);
        assertThat(projection.valorTotalVendas()).isEqualByComparingTo(valorTotal);
        assertThat(projection.dataVenda()).isEqualTo(dataVenda);
    }

    @Test
    void deve_garantir_que_todos_os_campos_da_venda_projection_nao_sao_nulos() {
        var projection = VendaProjection.builder()
                .id(1L)
                .usuarioId(1L)
                .usuario("Vendedor")
                .valor(BigDecimal.TEN)
                .quantidade(BigDecimal.ONE)
                .valorTotalVendas(BigDecimal.TEN)
                .dataVenda(LocalDateTime.now())
                .build();

        assertThat(projection.id()).isNotNull();
        assertThat(projection.usuarioId()).isNotNull();
        assertThat(projection.usuario()).isNotNull();
        assertThat(projection.valor()).isNotNull();
        assertThat(projection.quantidade()).isNotNull();
        assertThat(projection.valorTotalVendas()).isNotNull();
        assertThat(projection.dataVenda()).isNotNull();
    }

    @Test
    void deve_testar_igualdade_e_hashcode_do_record_venda_projection() {
        var dataVenda = LocalDateTime.now();
        var qtd = new BigDecimal("2.00");

        var v1 = VendaProjection.builder()
                .id(1L)
                .usuarioId(10L)
                .quantidade(qtd)
                .valor(BigDecimal.TEN)
                .valorTotalVendas(new BigDecimal("20.00"))
                .dataVenda(dataVenda)
                .build();

        var v2 = VendaProjection.builder()
                .id(1L)
                .usuarioId(10L)
                .quantidade(qtd)
                .valor(BigDecimal.TEN)
                .valorTotalVendas(new BigDecimal("20.00"))
                .dataVenda(dataVenda)
                .build();

        assertThat(v1).isEqualTo(v2);
        assertThat(v1.hashCode()).isEqualTo(v2.hashCode());
    }
}
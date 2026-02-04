package com.example.SalesHub.dto.request;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VendaRequestTest {

    @Test
    void deve_criar_venda_request_com_sucesso_usando_builder() {
        var usuarioId = 1L;
        var produtoId = 2L;
        var estoqueId = 3L;
        var quantidade = new BigDecimal("5.00");
        var valor = new BigDecimal("150.00");
        var desconto = 10.0;

        var request = VendaRequest.builder()
                .usuarioId(usuarioId)
                .produtoId(produtoId)
                .estoqueId(estoqueId)
                .quantidade(quantidade)
                .valor(valor)
                .desconto(desconto)
                .build();

        assertThat(request).isNotNull();
        assertThat(request.usuarioId()).isEqualTo(usuarioId);
        assertThat(request.produtoId()).isEqualTo(produtoId);
        assertThat(request.estoqueId()).isEqualTo(estoqueId);
        assertThat(request.quantidade()).isEqualByComparingTo(quantidade);
        assertThat(request.valor()).isEqualByComparingTo(valor);
        assertThat(request.desconto()).isEqualTo(desconto);
    }

    @Test
    void deve_garantir_que_todos_os_campos_da_venda_request_nao_sao_nulos() {
        var request = VendaRequest.builder()
                .usuarioId(1L)
                .produtoId(1L)
                .estoqueId(1L)
                .quantidade(BigDecimal.ONE)
                .valor(BigDecimal.TEN)
                .desconto(0.0)
                .build();

        assertThat(request.usuarioId()).isNotNull();
        assertThat(request.produtoId()).isNotNull();
        assertThat(request.estoqueId()).isNotNull();
        assertThat(request.quantidade()).isNotNull();
        assertThat(request.valor()).isNotNull();
        assertThat(request.desconto()).isNotNull();
    }

    @Test
    void deve_testar_igualdade_e_hashcode_do_record_venda_request() {
        var r1 = VendaRequest.builder()
                .usuarioId(1L)
                .produtoId(2L)
                .estoqueId(3L)
                .quantidade(BigDecimal.TEN)
                .valor(new BigDecimal("100.00"))
                .desconto(5.0)
                .build();

        var r2 = VendaRequest.builder()
                .usuarioId(1L)
                .produtoId(2L)
                .estoqueId(3L)
                .quantidade(BigDecimal.TEN)
                .valor(new BigDecimal("100.00"))
                .desconto(5.0)
                .build();

        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }
}
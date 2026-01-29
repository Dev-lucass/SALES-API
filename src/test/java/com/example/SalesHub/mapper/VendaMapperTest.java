package com.example.SalesHub.mapper;

import com.example.SalesHub.dto.request.VendaRequest;
import com.example.SalesHub.dto.response.entity.EstoqueResponse;
import com.example.SalesHub.dto.response.entity.ProdutoResponse;
import com.example.SalesHub.dto.response.entity.UsuarioResponse;
import com.example.SalesHub.model.Produto;
import com.example.SalesHub.model.Usuario;
import com.example.SalesHub.model.Venda;
import com.example.SalesHub.model.enums.StatusPagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import java.time.temporal.ChronoUnit;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VendaMapperTest {

    private VendaMapper vendaMapper;

    @BeforeEach
    void setup() {
        vendaMapper = new VendaMapper();
    }

    @Test
    void deve_mapear_venda_request_para_entidade_venda() {
        var request = VendaRequest.builder()
                .preco(new BigDecimal("150.00"))
                .build();

        var usuario = Usuario.builder().id(1L).nome("Teste").build();
        var produto = Produto.builder().id(2L).nome("Produto Teste").build();

        var entity = vendaMapper.toEntity(request, usuario, produto);

        assertThat(entity.getUsuario()).isEqualTo(usuario);
        assertThat(entity.getProduto()).isEqualTo(produto);
        assertThat(entity.getValor()).isEqualTo(new BigDecimal("150.00"));
        assertThat(entity.getStatusPagamento()).isEqualTo(StatusPagamento.PENDENTE);
        assertThat(entity.getDataVenda()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
    }

    @Test
    void deve_mapear_entidade_venda_para_venda_response() {
        var dataFixa = LocalDateTime.now();
        var venda = Venda.builder()
                .id(100L)
                .statusPagamento(StatusPagamento.CONCLUIDO)
                .dataVenda(dataFixa)
                .build();

        var usuarioRes = UsuarioResponse.builder().id(1L).build();
        var produtoRes = ProdutoResponse.builder().id(2L).build();
        var estoqueRes = EstoqueResponse.builder().id(3L).build();

        var response = vendaMapper.toReponse(venda, usuarioRes, produtoRes, estoqueRes);

        assertThat(response.id()).isEqualTo(100L);
        assertThat(response.usuario()).isEqualTo(usuarioRes);
        assertThat(response.produto()).isEqualTo(produtoRes);
        assertThat(response.estoque()).isEqualTo(estoqueRes);
        assertThat(response.statusPagamento()).isEqualTo(StatusPagamento.CONCLUIDO);
        assertThat(response.dataVenda()).isEqualTo(dataFixa);
    }

    @Test
    void deve_manter_consistencia_de_objetos_nulos_se_passados_ao_mapear_response() {
        var venda = Venda.builder().id(1L).build();

        var response = vendaMapper.toReponse(venda, null, null, null);

        assertThat(response.usuario()).isNull();
        assertThat(response.produto()).isNull();
        assertThat(response.estoque()).isNull();
    }
}
package com.example.SalesHub.mapper;

import com.example.SalesHub.dto.request.EstoqueRequest;
import com.example.SalesHub.model.Estoque;
import com.example.SalesHub.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EstoqueMapperTest {

    private EstoqueMapper mapper;
    private Produto produto;
    private EstoqueRequest request;
    private Estoque estoque;

    @BeforeEach
    void setup() {
        mapper = new EstoqueMapper();

        produto = Produto.builder()
                .id(10L)
                .nome("Produto Teste")
                .build();

        request = EstoqueRequest.builder()
                .produtoId(10L)
                .quantidade(new BigDecimal("100.00"))
                .build();

        estoque = Estoque.builder()
                .id(1L)
                .produto(produto)
                .quantidadeInicial(new BigDecimal("100.00"))
                .quantidadeAtual(new BigDecimal("100.00"))
                .build();
    }

    @Test
    void deve_converter_request_para_entidade() {
        var resultado = mapper.toEntity(request, produto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getProduto()).isEqualTo(produto);
        assertThat(resultado.getQuantidadeInicial()).isEqualByComparingTo(request.quantidade());
        assertThat(resultado.getQuantidadeAtual()).isEqualByComparingTo(request.quantidade());
    }

    @Test
    void deve_converter_entidade_para_response() {
        var resultado = mapper.toResponse(estoque);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(estoque.getId());
        assertThat(resultado.produtoId()).isEqualTo(produto.getId());
        assertThat(resultado.quantidadeInicial()).isEqualByComparingTo(estoque.getQuantidadeInicial());
        assertThat(resultado.quantidadeAtual()).isEqualByComparingTo(estoque.getQuantidadeAtual());
    }
}
package com.example.SalesHub.mapper;

import com.example.SalesHub.dto.request.EstoqueRequest;
import com.example.SalesHub.model.Estoque;
import com.example.SalesHub.model.Produto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EstoqueMapperTest {

    private final EstoqueMapper estoqueMapper = new EstoqueMapper();

    @Test
    void deve_converter_request_e_produto_para_entidade_estoque() {
        var produto = Produto.builder()
                .id(1L)
                .nome("Produto Teste")
                .build();

        var request = EstoqueRequest.builder()
                .produtoId(1L)
                .quantidade(50L)
                .build();

        var entidade = estoqueMapper.toEntity(request, produto);

        Assertions.assertThat(entidade)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.getProduto()).isEqualTo(produto);
                    Assertions.assertThat(saida.getQuantidadeInicial()).isEqualTo(50L);
                    Assertions.assertThat(saida.getQuantidadeAtual()).isEqualTo(50L);
                });
    }

    @Test
    void deve_converter_entidade_estoque_para_estoqueResponse() {
        var produto = Produto.builder()
                .id(10L)
                .build();

        var estoque = Estoque.builder()
                .id(1L)
                .produto(produto)
                .quantidadeInicial(100L)
                .quantidadeAtual(80L)
                .build();

        var response = estoqueMapper.toResponse(estoque);

        Assertions.assertThat(response)
                .isNotNull()
                .satisfies(saida -> {
                    Assertions.assertThat(saida.id()).isEqualTo(1L);
                    Assertions.assertThat(saida.produtoId()).isEqualTo(10L);
                    Assertions.assertThat(saida.quantidadeInicial()).isEqualTo(100L);
                    Assertions.assertThat(saida.quantidadeAtual()).isEqualTo(80L);
                });
    }

    @Test
    void deve_garantir_que_quantidade_inicial_e_atual_sao_iguais_na_criacao() {
        var produto = Produto.builder().build();
        var request = EstoqueRequest.builder()
                .quantidade(100L)
                .build();

        var entidade = estoqueMapper.toEntity(request, produto);

        Assertions.assertThat(entidade.getQuantidadeInicial())
                .isEqualTo(entidade.getQuantidadeAtual())
                .isEqualTo(100L);
    }
}
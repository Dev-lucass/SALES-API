package com.example.SalesHub.mapper;

import com.example.SalesHub.dto.request.ProdutoRequest;
import com.example.SalesHub.model.Produto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProdutoMapperTest {

    private final ProdutoMapper mapper = new ProdutoMapper();

    @Test
    void deve_converter_produtoRequest_para_entidade() {
        var request = ProdutoRequest.builder()
                .nome("Cadeira")
                .descricao("Cadeira de escritório")
                .preco(BigDecimal.valueOf(500.00))
                .build();

        var entidade = mapper.toEntity(request);

        Assertions.assertThat(entidade).isNotNull();
        Assertions.assertThat(entidade.getNome()).isEqualTo(request.nome());
        Assertions.assertThat(entidade.getDescricao()).isEqualTo(request.descricao());
        Assertions.assertThat(entidade.getPreco())
                .usingComparator(BigDecimal::compareTo)
                .isEqualTo(request.preco());
    }

    @Test
    void deve_converter_entidade_para_produtoResponse() {
        var agora = LocalDateTime.of(2024, 1, 20, 10, 0, 0);
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        var entidade = Produto.builder()
                .id(1L)
                .nome("Mouse")
                .descricao("Mouse sem fio")
                .preco(BigDecimal.valueOf(150.00))
                .criadoEm(agora)
                .build();

        var response = mapper.toResponse(entidade);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.id()).isEqualTo(entidade.getId());
        Assertions.assertThat(response.nome()).isEqualTo(entidade.getNome());
        Assertions.assertThat(response.descricao()).isEqualTo(entidade.getDescricao());
        Assertions.assertThat(response.preco())
                .usingComparator(BigDecimal::compareTo)
                .isEqualTo(entidade.getPreco());
        Assertions.assertThat(response.criadoEm().format(formatter))
                .isEqualTo("20/01/2024 10:00:00");
    }
}
package com.example.SalesHub.repository.customImpl;

import com.example.SalesHub.configuration.JpaQueryFactoryConfig;
import com.example.SalesHub.dto.filter.ProdutoFilter;
import com.example.SalesHub.model.Produto;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@DataJpaTest
@Import({ProdutoRepositoryCustom.class, JpaQueryFactoryConfig.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProdutoRepositoryCustomTest {

    @Autowired
    private ProdutoRepositoryCustom repositoryCustom;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setup() {
        var produto = Produto.builder()
                .nome("Teclado Gamer")
                .descricao("Teclado Mecânico RGB")
                .preco(BigDecimal.valueOf(250.00))
                .ativo(true)
                .criadoEm(LocalDateTime.now())
                .build();
        entityManager.persist(produto);
    }

    @Test
    void deve_buscar_produto_duplicado_por_nome() {
        var produtoDuplicado = Produto.builder()
                .nome("Teclado Gamer")
                .build();

        var resultado = repositoryCustom.buscarProdutoDuplicado(produtoDuplicado);

        Assertions.assertThat(resultado).isPresent();
        Assertions.assertThat(resultado.get().getNome()).isEqualTo("Teclado Gamer");
    }

    @Test
    void deve_buscar_produtos_por_filtro_de_nome_ignore_case() {
        var filter = ProdutoFilter.builder()
                .nome("teclado")
                .build();
        var pageable = PageRequest.of(0, 10);

        var resultado = repositoryCustom.buscarProdutos(filter, pageable);

        Assertions.assertThat(resultado.getContent()).hasSize(1);
        Assertions.assertThat(resultado.getContent().get(0).nome()).isEqualTo("Teclado Gamer");
    }

    @Test
    void deve_buscar_produtos_por_filtro_de_preco() {
        var filter = ProdutoFilter.builder()
                .preco(BigDecimal.valueOf(250.00))
                .build();
        var pageable = PageRequest.of(0, 10);

        var resultado = repositoryCustom.buscarProdutos(filter, pageable);

        Assertions.assertThat(resultado.getContent()).isNotEmpty();
        Assertions.assertThat(resultado.getContent().get(0).preco())
                .usingComparator(BigDecimal::compareTo)
                .isEqualTo(BigDecimal.valueOf(250.00));
    }

    @Test
    void deve_buscar_produtos_por_intervalo_de_datas() {
        var hoje = LocalDate.now();
        var filter = ProdutoFilter.builder()
                .dataInicial(hoje)
                .dataFinal(hoje)
                .build();
        var pageable = PageRequest.of(0, 10);

        var resultado = repositoryCustom.buscarProdutos(filter, pageable);

        Assertions.assertThat(resultado.getContent()).isNotEmpty();
    }

    @Test
    void deve_buscar_produto_existente_por_id() {
        var produto = Produto.builder()
                .nome("Monitor 144hz")
                .descricao("Monitor Gamer")
                .preco(BigDecimal.valueOf(1200.00))
                .ativo(true)
                .build();
        entityManager.persist(produto);

        var id = produto.getId();

        var resultado = repositoryCustom.buscarProdutoExistente(id);

        Assertions.assertThat(resultado).isPresent();
        Assertions.assertThat(resultado.get().getNome()).isEqualTo("Monitor 144hz");
    }

    @Test
    void deve_retornar_vazio_quando_produto_nao_existir() {
        var resultado = repositoryCustom.buscarProdutoExistente(999L);
        Assertions.assertThat(resultado).isEmpty();
    }
}
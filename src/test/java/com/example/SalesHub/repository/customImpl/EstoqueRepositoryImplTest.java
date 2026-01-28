package com.example.SalesHub.repository.customImpl;

import com.example.SalesHub.configuration.JpaQueryFactoryConfig;
import com.example.SalesHub.dto.filter.EstoqueFilter;
import com.example.SalesHub.model.Estoque;
import com.example.SalesHub.model.Produto;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({EstoqueRepositoryImpl.class, JpaQueryFactoryConfig.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EstoqueRepositoryImplTest {

    @Autowired
    private EstoqueRepositoryImpl repositoryCustom;

    @Autowired
    private EntityManager entityManager;

    private Produto produtoSalvo;

    @BeforeEach
    void setup() {
        produtoSalvo = Produto.builder()
                .nome("Produto Base")
                .descricao("Descricao Base")
                .ativo(true)
                .criadoEm(LocalDateTime.now())
                .build();

        entityManager.persist(produtoSalvo);

        var estoque = Estoque.builder()
                .produto(produtoSalvo)
                .quantidadeInicial(100L)
                .quantidadeAtual(100L)
                .ativo(true)
                .build();

        entityManager.persist(estoque);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void deve_buscar_estoque_duplicado() {
        var estoqueParaComparar = Estoque.builder()
                .produto(produtoSalvo)
                .build();

        var resultado = repositoryCustom.buscarEstoqueDuplicado(estoqueParaComparar);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getProduto().getId()).isEqualTo(produtoSalvo.getId());
    }

    @Test
    void deve_buscar_estoques_por_filtro_de_produtoId() {
        var filter = EstoqueFilter.builder()
                .produtoId(produtoSalvo.getId())
                .build();

        var pageable = PageRequest.of(0, 10);

        var resultado = repositoryCustom.buscarEstoques(filter, pageable);

        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().getFirst().produtoId()).isEqualTo(produtoSalvo.getId());
    }

    @Test
    void deve_buscar_estoques_por_filtro_de_quantidade() {
        var filter = EstoqueFilter.builder()
                .quantidadeAtual(100L)
                .build();

        var pageable = PageRequest.of(0, 10);

        var resultado = repositoryCustom.buscarEstoques(filter, pageable);

        assertThat(resultado.getContent()).isNotEmpty();
        assertThat(resultado.getContent().getFirst().quantidadeAtual()).isEqualTo(100L);
    }

    @Test
    void deve_buscar_estoque_existente_por_id() {
        var novoProduto = Produto.builder()
                .nome("Novo Produto")
                .descricao("Nova Descricao")
                .ativo(true)
                .criadoEm(LocalDateTime.now())
                .build();

        entityManager.persist(novoProduto);

        var estoque = Estoque.builder()
                .produto(novoProduto)
                .quantidadeInicial(10L)
                .quantidadeAtual(10L)
                .ativo(true)
                .build();

        entityManager.persist(estoque);
        entityManager.flush();

        var idParaBusca = estoque.getId();

        entityManager.clear();

        var resultado = repositoryCustom.buscarEstoqueExistente(idParaBusca);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(idParaBusca);
    }

    @Test
    void deve_retornar_vazio_quando_estoque_for_inativo_ou_inexistente() {
        var resultado = repositoryCustom.buscarEstoqueExistente(999L);
        assertThat(resultado).isEmpty();
    }
}
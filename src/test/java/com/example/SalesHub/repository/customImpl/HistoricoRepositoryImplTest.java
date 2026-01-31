package com.example.SalesHub.repository.customImpl;

import com.example.SalesHub.configuration.JpaQueryFactoryConfig;
import com.example.SalesHub.dto.filter.HistoricoFilter;
import com.example.SalesHub.model.Estoque;
import com.example.SalesHub.model.Historico;
import com.example.SalesHub.model.Produto;
import com.example.SalesHub.model.Usuario;
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
@Import({HistoricoRepositoryImpl.class, JpaQueryFactoryConfig.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HistoricoRepositoryImplTest {

    @Autowired
    private HistoricoRepositoryImpl repositoryCustom;

    @Autowired
    private EntityManager entityManager;

    private Historico historicoPersistido;

    @BeforeEach
    void setup() {
        var usuario = Usuario.builder()
                .nome("Admin")
                .email("admin@email.com")
                .senha("123456")
                .ativo(true)
                .build();
        entityManager.persist(usuario);

        var produto = Produto.builder()
                .nome("Notebook")
                .descricao("Descricao do Produto")
                .ativo(true)
                .build();
        entityManager.persist(produto);

        var estoque = Estoque.builder()
                .produto(produto)
                .quantidadeAtual(10L)
                .quantidadeInicial(10L)
                .ativo(true)
                .build();
        entityManager.persist(estoque);

        historicoPersistido = Historico.builder()
                .usuario(usuario)
                .produto(produto)
                .estoque(estoque)
                .criadoEm(LocalDateTime.now())
                .build();

        entityManager.persist(historicoPersistido);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void deve_buscar_historico_filtrando_por_id() {
        var filter = HistoricoFilter.builder()
                .id(historicoPersistido.getId())
                .build();
        var pageable = PageRequest.of(0, 10);

        var resultado = repositoryCustom.buscarHistorico(filter, pageable);

        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().getFirst().id()).isEqualTo(historicoPersistido.getId());
    }

    @Test
    void deve_buscar_historico_filtrando_por_usuario_id() {
        var filter = HistoricoFilter.builder()
                .usuarioId(historicoPersistido.getUsuario().getId())
                .build();
        var pageable = PageRequest.of(0, 10);

        var resultado = repositoryCustom.buscarHistorico(filter, pageable);

        assertThat(resultado.getContent()).isNotEmpty();
        assertThat(resultado.getContent().getFirst().usuarioId()).isEqualTo(historicoPersistido.getUsuario().getId());
    }

    @Test
    void deve_buscar_historico_filtrando_por_produto_id() {
        var filter = HistoricoFilter.builder()
                .produtoId(historicoPersistido.getProduto().getId())
                .build();
        var pageable = PageRequest.of(0, 10);

        var resultado = repositoryCustom.buscarHistorico(filter, pageable);

        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().getFirst().produtoId()).isEqualTo(historicoPersistido.getProduto().getId());
    }

    @Test
    void deve_retornar_vazio_quando_nenhum_historico_corresponder_ao_filtro() {
        var filter = HistoricoFilter.builder()
                .id(999L)
                .build();
        var pageable = PageRequest.of(0, 10);

        var resultado = repositoryCustom.buscarHistorico(filter, pageable);

        assertThat(resultado.getContent()).isEmpty();
        assertThat(resultado.getTotalElements()).isZero();
    }

    @Test
    void deve_respeitar_paginacao_na_consulta_de_historico() {
        var filter = HistoricoFilter.builder().build();
        var pageable = PageRequest.of(0, 1);

        var resultado = repositoryCustom.buscarHistorico(filter, pageable);

        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getSize()).isEqualTo(1);
    }
}
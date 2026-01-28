package com.example.SalesHub.repository.customImpl;

import com.example.SalesHub.configuration.JpaQueryFactoryConfig;
import com.example.SalesHub.dto.filter.VendaFilter;
import com.example.SalesHub.model.Produto;
import com.example.SalesHub.model.Usuario;
import com.example.SalesHub.model.Venda;
import com.example.SalesHub.model.enums.StatusPagamento;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({VendaRepositoryImpl.class, JpaQueryFactoryConfig.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VendaRepositoryImplTest {

    @Autowired
    private VendaRepositoryImpl repositoryCustom;

    @Autowired
    private EntityManager entityManager;

    private Usuario usuarioSalvo;

    @BeforeEach
    void setup() {
        usuarioSalvo = Usuario.builder()
                .nome("Usuario Teste")
                .email("teste@saleshub.com")
                .senha("123456")
                .ativo(true)
                .build();
        entityManager.persist(usuarioSalvo);

        var produtoSalvo = Produto.builder()
                .nome("Produto Teste")
                .descricao("Descrição obrigatória")
                .ativo(true)
                .criadoEm(LocalDateTime.now())
                .build();
        entityManager.persist(produtoSalvo);

        var venda = Venda.builder()
                .usuario(usuarioSalvo)
                .produto(produtoSalvo)
                .valor(new BigDecimal("150.00")) // Sincronizado com a Entidade
                .statusPagamento(StatusPagamento.PENDENTE)
                .dataVenda(LocalDateTime.now())
                .build();

        entityManager.persist(venda);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void deve_buscar_vendas_paginadas_com_sucesso() {
        var filter = VendaFilter.builder().build();
        var pageable = PageRequest.of(0, 10);

        var resultado = repositoryCustom.buscarVendas(filter, pageable);

        assertThat(resultado.getContent()).isNotEmpty();
        assertThat(resultado.getTotalElements()).isGreaterThanOrEqualTo(1);

        var projecao = resultado.getContent().getFirst();

        assertThat(projecao.usuarioId()).isEqualTo(usuarioSalvo.getId());
        assertThat(projecao.valor()).isEqualByComparingTo("150.00");
        assertThat(projecao.statusPagamento()).isEqualTo(StatusPagamento.PENDENTE);
    }

    @Test
    void deve_retornar_pagina_vazia_quando_nao_houver_vendas_cadastradas() {
        // Limpeza explícita para garantir vacuidade
        entityManager.createQuery("DELETE FROM Venda").executeUpdate();

        var filter = VendaFilter.builder().build();
        var pageable = PageRequest.of(0, 10);

        var resultado = repositoryCustom.buscarVendas(filter, pageable);

        assertThat(resultado.getContent()).isEmpty();
        assertThat(resultado.getTotalElements()).isZero();
    }
}
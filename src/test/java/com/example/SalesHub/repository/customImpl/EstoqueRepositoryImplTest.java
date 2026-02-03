package com.example.SalesHub.repository.customImpl;

import com.example.SalesHub.dto.filter.EstoqueFilter;
import com.example.SalesHub.dto.projection.EstoqueProjection;
import com.example.SalesHub.model.Estoque;
import com.example.SalesHub.model.Produto;
import com.example.SalesHub.model.QEstoque;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EstoqueRepositoryImplTest {

    @Mock
    private JPAQueryFactory queryFactory;

    @Mock
    private JPAQuery<Object> jpaQuery;

    @Mock
    private JPAQuery<Long> countQuery;

    @InjectMocks
    private EstoqueRepositoryImpl repository;

    private Estoque estoque;
    private Produto produto;
    private EstoqueFilter filter;
    private Pageable pageable;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setup() {
        produto = Produto.builder()
                .id(10L)
                .nome("Produto Teste")
                .descricao("Descricao do Produto")
                .ativo(true)
                .build();

        estoque = Estoque.builder()
                .id(1L)
                .produto(produto)
                .quantidadeInicial(new BigDecimal("100.00"))
                .quantidadeAtual(new BigDecimal("100.00"))
                .ativo(true)
                .build();

        filter = EstoqueFilter.builder()
                .id(1L)
                .produtoId(10L)
                .quantidadeInicial(new BigDecimal("100.00"))
                .quantidadeAtual(new BigDecimal("100.00"))
                .build();

        pageable = PageRequest.of(0, 10);
    }

    @Test
    @SuppressWarnings("unchecked")
    void deve_buscar_estoque_duplicado_com_sucesso() {
        when(queryFactory.selectFrom(any(QEstoque.class))).thenReturn((JPAQuery) jpaQuery);
        when(jpaQuery.where(any(Predicate.class))).thenReturn(jpaQuery);
        when(jpaQuery.fetchFirst()).thenReturn(estoque);

        var resultado = repository.buscarEstoqueDuplicado(estoque);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1L);
    }

    @Test
    @SuppressWarnings("unchecked")
    void deve_buscar_estoques_paginado_com_todos_os_filtros() {
        var projection = new EstoqueProjection(1L, 10L, new BigDecimal("100.00"), new BigDecimal("100.00"));

        lenient().when(queryFactory.select(any(Expression.class)))
                .thenReturn((JPAQuery) jpaQuery)
                .thenReturn((JPAQuery) countQuery);

        lenient().when(jpaQuery.from(any(QEstoque.class))).thenReturn(jpaQuery);
        lenient().when(jpaQuery.where(any(Predicate.class))).thenReturn(jpaQuery);
        lenient().when(jpaQuery.orderBy(any(OrderSpecifier.class))).thenReturn(jpaQuery);
        lenient().when(jpaQuery.offset(anyLong())).thenReturn(jpaQuery);
        lenient().when(jpaQuery.limit(anyLong())).thenReturn(jpaQuery);
        lenient().when(jpaQuery.fetch()).thenReturn(List.of(projection));

        lenient().when(countQuery.from(any(QEstoque.class))).thenReturn(countQuery);
        lenient().when(countQuery.where(any(Predicate.class))).thenReturn(countQuery);
        lenient().when(countQuery.fetchFirst()).thenReturn(1L);

        var resultado = repository.buscarEstoques(filter, pageable);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getTotalElements()).isEqualTo(1L);
        assertThat(resultado.getContent().get(0).id()).isEqualTo(1L);
    }

    @Test
    @SuppressWarnings("unchecked")
    void deve_buscar_estoque_existente_por_id_com_sucesso() {
        when(queryFactory.selectFrom(any(QEstoque.class))).thenReturn((JPAQuery) jpaQuery);
        when(jpaQuery.where(any(Predicate.class), any(Predicate.class))).thenReturn(jpaQuery);
        when(jpaQuery.fetchFirst()).thenReturn(estoque);

        var resultado = repository.buscarEstoqueExistente(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getAtivo()).isTrue();
    }

    @Test
    @SuppressWarnings("unchecked")
    void deve_retornar_vazio_quando_estoque_duplicado_nao_for_encontrado() {
        when(queryFactory.selectFrom(any(QEstoque.class))).thenReturn((JPAQuery) jpaQuery);
        when(jpaQuery.where(any(Predicate.class))).thenReturn(jpaQuery);
        when(jpaQuery.fetchFirst()).thenReturn(null);

        var resultado = repository.buscarEstoqueDuplicado(estoque);

        assertThat(resultado).isEmpty();
    }
}
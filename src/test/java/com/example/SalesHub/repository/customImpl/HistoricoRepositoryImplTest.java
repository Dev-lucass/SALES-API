package com.example.SalesHub.repository.customImpl;

import com.example.SalesHub.dto.filter.HistoricoFilter;
import com.example.SalesHub.dto.projection.HistoricoProjection;
import com.example.SalesHub.model.QHistorico;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HistoricoRepositoryImplTest {

    @Mock
    private JPAQueryFactory queryFactory;

    @Mock
    private JPAQuery<HistoricoProjection> jpaQuery;

    @Mock
    private JPAQuery<Long> countQuery;

    @InjectMocks
    private HistoricoRepositoryImpl repository;

    private HistoricoFilter filter;
    private Pageable pageable;
    private HistoricoProjection projection;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setup() {
        var quantidade = new BigDecimal("5.00");

        filter = HistoricoFilter.builder()
                .id(1L)
                .usuarioId(10L)
                .produtoId(20L)
                .estoqueId(30L)
                .quantidadeRetirada(quantidade)
                .build();

        pageable = PageRequest.of(0, 10);

        projection = new HistoricoProjection(
                1L,
                10L,
                20L,
                30L,
                quantidade,
                LocalDateTime.now()
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    void deve_buscar_historico_com_todos_os_filtros_e_retornar_pagina() {
        lenient().when(queryFactory.select(any(Expression.class))).thenReturn((JPAQuery) jpaQuery).thenReturn((JPAQuery) countQuery);

        lenient().when(jpaQuery.from(any(QHistorico.class))).thenReturn(jpaQuery);
        lenient().when(jpaQuery.where(any(Predicate.class))).thenReturn(jpaQuery);
        lenient().when(jpaQuery.orderBy(any(OrderSpecifier.class))).thenReturn(jpaQuery);
        lenient().when(jpaQuery.offset(anyLong())).thenReturn(jpaQuery);
        lenient().when(jpaQuery.limit(anyLong())).thenReturn(jpaQuery);
        lenient().when(jpaQuery.fetch()).thenReturn(List.of(projection));

        lenient().when(countQuery.from(any(QHistorico.class))).thenReturn(countQuery);
        lenient().when(countQuery.where(any(Predicate.class))).thenReturn(countQuery);
        lenient().when(countQuery.fetchFirst()).thenReturn(1L);

        var resultado = repository.buscarHistorico(filter, pageable);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getTotalElements()).isEqualTo(1L);
        assertThat(resultado.getContent().get(0).id()).isEqualTo(1L);
    }

    @Test
    @SuppressWarnings("unchecked")
    void deve_buscar_historico_sem_filtros_com_sucesso() {
        var filtroVazio = HistoricoFilter.builder().build();

        lenient().when(queryFactory.select(any(Expression.class))).thenReturn((JPAQuery) jpaQuery).thenReturn((JPAQuery) countQuery);

        lenient().when(jpaQuery.from(any(QHistorico.class))).thenReturn(jpaQuery);
        lenient().when(jpaQuery.where(any(Predicate.class))).thenReturn(jpaQuery);
        lenient().when(jpaQuery.orderBy(any(OrderSpecifier.class))).thenReturn(jpaQuery);
        lenient().when(jpaQuery.offset(anyLong())).thenReturn(jpaQuery);
        lenient().when(jpaQuery.limit(anyLong())).thenReturn(jpaQuery);
        lenient().when(jpaQuery.fetch()).thenReturn(List.of());

        lenient().when(countQuery.from(any(QHistorico.class))).thenReturn(countQuery);
        lenient().when(countQuery.where(any(Predicate.class))).thenReturn(countQuery);
        lenient().when(countQuery.fetchFirst()).thenReturn(0L);

        var resultado = repository.buscarHistorico(filtroVazio, pageable);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getTotalElements()).isZero();
    }
}
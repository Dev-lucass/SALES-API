package com.example.SalesHub.repository.customImpl;

import com.example.SalesHub.dto.filter.VendaFilter;
import com.example.SalesHub.dto.projection.VendaProjection;
import com.example.SalesHub.model.QVenda;
import com.example.SalesHub.model.Usuario;
import com.example.SalesHub.model.Venda;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VendaRepositoryImplTest {

    @Mock
    private JPAQueryFactory queryFactory;

    @Mock
    private JPAQuery<VendaProjection> jpaQuery;

    @Mock
    private JPAQuery<Long> countQuery;

    @Mock
    private JPAQuery<BigDecimal> sumQuery;

    @InjectMocks
    private VendaRepositoryImpl repository;

    private VendaFilter filter;
    private Pageable pageable;
    private Venda venda;
    private Usuario usuario;
    private VendaProjection projection;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setup() {
        usuario = Usuario.builder()
                .id(1L)
                .nome("Junior Testador")
                .email("junior@saleshub.com")
                .build();

        venda = Venda.builder()
                .id(100L)
                .usuario(usuario)
                .valor(new BigDecimal("200.00"))
                .quantidade(new BigDecimal("2.00"))
                .dataVenda(LocalDateTime.now())
                .build();

        filter = VendaFilter.builder()
                .id(100L)
                .usuarioId(1L)
                .valor(new BigDecimal("100.00"))
                .quantidade(new BigDecimal("2.00"))
                .valorTotalVendas(new BigDecimal("400.00"))
                .dataInicial(LocalDate.now().minusDays(2))
                .dataFinal(LocalDate.now())
                .build();

        pageable = PageRequest.of(0, 10);

        projection = VendaProjection.builder()
                .id(100L)
                .usuarioId(1L)
                .usuario("Junior Testador")
                .valor(new BigDecimal("200.00"))
                .quantidade(new BigDecimal("2.00"))
                .valorTotalVendas(new BigDecimal("400.00"))
                .dataVenda(LocalDateTime.now())
                .build();
    }

    @Test
    @SuppressWarnings("unchecked")
    void deve_buscar_vendas_com_sucesso_usando_filtros() {
        lenient().when(queryFactory.select(any(Expression.class))).thenReturn((JPAQuery) jpaQuery).thenReturn((JPAQuery) countQuery);

        lenient().when(jpaQuery.from(any(QVenda.class))).thenReturn(jpaQuery);
        lenient().when(jpaQuery.where(any(Predicate.class))).thenReturn(jpaQuery);
        lenient().when(jpaQuery.orderBy(any(OrderSpecifier.class))).thenReturn(jpaQuery);
        lenient().when(jpaQuery.offset(anyLong())).thenReturn(jpaQuery);
        lenient().when(jpaQuery.limit(anyLong())).thenReturn(jpaQuery);
        lenient().when(jpaQuery.fetch()).thenReturn(List.of(projection));

        lenient().when(countQuery.from(any(QVenda.class))).thenReturn(countQuery);
        lenient().when(countQuery.where(any(Predicate.class))).thenReturn(countQuery);
        lenient().when(countQuery.fetchFirst()).thenReturn(1L);

        var resultado = repository.buscarVendas(filter, pageable);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getTotalElements()).isEqualTo(1L);
        assertThat(resultado.getContent().get(0).id()).isEqualTo(100L);
    }

    @Test
    @SuppressWarnings("unchecked")
    void deve_buscar_valor_total_de_vendas_por_usuario() {
        when(queryFactory.select(any(Expression.class))).thenReturn((JPAQuery) sumQuery);
        when(sumQuery.from(any(QVenda.class))).thenReturn(sumQuery);
        when(sumQuery.where(any(Predicate.class))).thenReturn(sumQuery);
        when(sumQuery.fetchFirst()).thenReturn(new BigDecimal("1000.00"));

        var resultado = repository.buscarValorTotalDeVendas(venda);

        assertThat(resultado).isEqualByComparingTo("1000.00");
    }

    @Test
    @SuppressWarnings("unchecked")
    void deve_retornar_pagina_vazia_quando_nenhuma_venda_for_encontrada() {
        var filtroVazio = VendaFilter.builder().build();

        lenient().when(queryFactory.select(any(Expression.class))).thenReturn((JPAQuery) jpaQuery).thenReturn((JPAQuery) countQuery);

        lenient().when(jpaQuery.from(any(QVenda.class))).thenReturn(jpaQuery);
        lenient().when(jpaQuery.where(any(Predicate.class))).thenReturn(jpaQuery);
        lenient().when(jpaQuery.orderBy(any(OrderSpecifier.class))).thenReturn(jpaQuery);
        lenient().when(jpaQuery.offset(anyLong())).thenReturn(jpaQuery);
        lenient().when(jpaQuery.limit(anyLong())).thenReturn(jpaQuery);
        lenient().when(jpaQuery.fetch()).thenReturn(List.of());

        lenient().when(countQuery.from(any(QVenda.class))).thenReturn(countQuery);
        lenient().when(countQuery.where(any(Predicate.class))).thenReturn(countQuery);
        lenient().when(countQuery.fetchFirst()).thenReturn(0L);

        var resultado = repository.buscarVendas(filtroVazio, pageable);

        assertThat(resultado.getTotalElements()).isZero();
    }
}
package com.example.SalesHub.repository.customImpl;

import com.example.SalesHub.dto.filter.VendaFilter;
import com.example.SalesHub.dto.projection.VendaProjection;
import com.example.SalesHub.model.QVenda;
import com.example.SalesHub.repository.custom.CustomVendaRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Optional;

@Repository
public class VendaRepositoryImpl implements CustomVendaRepository {

    @Autowired
    private JPAQueryFactory query;

    @Override
    public Page<VendaProjection> buscarVendas(VendaFilter filter, Pageable pageable) {
        var qVenda = QVenda.venda;
        var builder = new BooleanBuilder();

        Optional.ofNullable(filter.id())
                .ifPresent(id -> builder.and(qVenda.id.eq(id)));

        Optional.ofNullable(filter.usuarioId())
                .ifPresent(usuarioId -> builder.and(qVenda.usuario.id.eq(usuarioId)));

        Optional.ofNullable(filter.valor())
                .ifPresent(valor -> builder.and(qVenda.valor.goe(valor)));

        Optional.ofNullable(filter.dataInicial())
                .ifPresent(inicio -> builder.and(qVenda.dataVenda.goe(inicio.atStartOfDay())));

        Optional.ofNullable(filter.dataFinal())
                .ifPresent(fim -> builder.and(qVenda.dataVenda.loe(fim.atTime(LocalTime.MAX))));

        var consulta = query
                .select(Projections.constructor(VendaProjection.class,
                        qVenda.id,
                        qVenda.usuario.id,
                        qVenda.usuario.nome,
                        qVenda.valor,
                        qVenda.statusPagamento,
                        qVenda.dataVenda
                ))
                .from(qVenda)
                .where(builder)
                .orderBy(qVenda.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(consulta, pageable, () -> buscarQuantidadeDeVendas(builder));
    }

    private Long buscarQuantidadeDeVendas(BooleanBuilder builder) {
        var qVenda = QVenda.venda;
        return query
                .select(qVenda.id.countDistinct())
                .from(qVenda)
                .where(builder)
                .fetchOne();
    }
}
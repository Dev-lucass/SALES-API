package com.example.SalesHub.repository.customImpl;

import com.example.SalesHub.dto.filter.VendaFilter;
import com.example.SalesHub.dto.projection.VendaProjection;
import com.example.SalesHub.model.QVenda;
import com.example.SalesHub.model.Venda;
import com.example.SalesHub.repository.custom.CustomVendaRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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

        Optional.ofNullable(filter.id()).ifPresent(id -> builder.and(qVenda.id.eq(id)));
        Optional.ofNullable(filter.usuarioId()).ifPresent(uId -> builder.and(qVenda.usuario.id.eq(uId)));
        Optional.ofNullable(filter.valor()).ifPresent(v -> builder.and(qVenda.valor.goe(v)));
        Optional.ofNullable(filter.quantidade()).ifPresent(q -> builder.and(qVenda.quantidade.eq(q)));

        Optional.ofNullable(filter.valorTotalVendas())
                .ifPresent(total -> builder.and(qVenda.valorTotalVendas.goe(total)));

        Optional.ofNullable(filter.dataInicial()).ifPresent(i -> builder.and(qVenda.dataVenda.goe(i.atStartOfDay())));
        Optional.ofNullable(filter.dataFinal()).ifPresent(f -> builder.and(qVenda.dataVenda.loe(f.atTime(LocalTime.MAX))));

        var consulta = query
                .select(Projections.constructor(VendaProjection.class,
                        qVenda.id,
                        qVenda.usuario.id,
                        qVenda.usuario.nome,
                        qVenda.valor,
                        qVenda.quantidade, 
                        qVenda.valorTotalVendas,
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

    @Override
    public BigDecimal buscarValorTotalDeVendas(Venda venda) {
        var qVenda = QVenda.venda;
        return query
                .select(qVenda.valor.sum())
                .from(qVenda)
                .where(qVenda.usuario.id.eq(venda.getUsuario().getId()))
                .fetchFirst();
    }

    private Long buscarQuantidadeDeVendas(BooleanBuilder builder) {
        var qVenda = QVenda.venda;
        return query
                .select(qVenda.id.countDistinct())
                .from(qVenda)
                .where(builder)
                .fetchFirst();
    }
}
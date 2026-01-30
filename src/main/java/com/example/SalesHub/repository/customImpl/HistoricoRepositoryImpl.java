package com.example.SalesHub.repository.customImpl;

import com.example.SalesHub.dto.filter.HistoricoFilter;
import com.example.SalesHub.dto.projection.EstoqueProjection;
import com.example.SalesHub.dto.projection.HistoricoProjection;
import com.example.SalesHub.dto.projection.ProdutoProjection;
import com.example.SalesHub.dto.projection.UsuarioProjection;
import com.example.SalesHub.model.QHistorico;
import com.example.SalesHub.repository.custom.CustomHistoricoRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class HistoricoRepositoryImpl implements CustomHistoricoRepository {

    @Autowired
    private JPAQueryFactory query;

    @Override
    public Page<HistoricoProjection> buscarHistorico(HistoricoFilter filter, Pageable pageable) {

        var builder = new BooleanBuilder();
        var qHistorico = QHistorico.historico;

        Optional.ofNullable(filter.id())
                .ifPresent(historicoId -> builder.and(qHistorico.id.eq(historicoId)));

        Optional.ofNullable(filter.usuarioId())
                .ifPresent(usuarioId -> builder.and(qHistorico.usuario.id.eq(usuarioId)));

        Optional.ofNullable(filter.produtoId())
                .ifPresent(produtoId -> builder.and(qHistorico.produto.id.eq(produtoId)));

        Optional.ofNullable(filter.estoqueId())
                .ifPresent(estoqueId -> builder.and(qHistorico.estoque.id.eq(estoqueId)));

        var consulta = query
                .select(Projections.constructor(

                                HistoricoProjection.class,
                                qHistorico.id,

                                Projections.constructor(
                                        UsuarioProjection.class,
                                        qHistorico.usuario.id,
                                        qHistorico.usuario.nome,
                                        qHistorico.usuario.email,
                                        qHistorico.usuario.criadoEm
                                ),
                                Projections.constructor(
                                        ProdutoProjection.class,
                                        qHistorico.produto.id,
                                        qHistorico.produto.nome,
                                        qHistorico.produto.descricao,
                                        qHistorico.produto.criadoEm
                                ),
                                Projections.constructor(
                                        EstoqueProjection.class,
                                        qHistorico.estoque.id,
                                        qHistorico.estoque.produto.id,
                                        qHistorico.estoque.quantidadeInicial,
                                        qHistorico.estoque.quantidadeAtual
                                ),

                                qHistorico.criadoEm
                        ))
                .from(qHistorico)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qHistorico.id.asc())
                .fetch();

        return PageableExecutionUtils.getPage(consulta, pageable, this::buscarQuantidadeDeHistorico);
    }

    private Long buscarQuantidadeDeHistorico() {

        var qHistorico = QHistorico.historico;

        return query
                .select(qHistorico.id.countDistinct())
                .from(qHistorico)
                .fetchOne();
    }
}
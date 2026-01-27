package com.example.SalesHub.repository.customImpl;

import com.example.SalesHub.dto.filter.EstoqueFilter;
import com.example.SalesHub.dto.projection.EstoqueProjection;
import com.example.SalesHub.model.Estoque;
import com.example.SalesHub.model.QEstoque;
import com.example.SalesHub.repository.custom.CustomEstoqueRepository;
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
public class EstoqueRepositoryImpl implements CustomEstoqueRepository {

    @Autowired
    private JPAQueryFactory query;

    @Override
    public Optional<Estoque> buscarEstoqueDuplicado(Estoque estoque) {

        var builder = new BooleanBuilder();
        var qEstoque = QEstoque.estoque;

        builder.and(qEstoque.produto.eq(estoque.getProduto()));

        if (estoque.getId() != null) {
            builder.and(qEstoque.id.notIn(estoque.getId()));
        }

        var consulta =  query
                .selectFrom(qEstoque)
                .where(builder)
                .fetchOne();

        return Optional.ofNullable(consulta);
    }

    @Override
    public Page<EstoqueProjection> buscarEstoques(EstoqueFilter filter, Pageable pageable) {

        var builder = new BooleanBuilder();
        var qEstoque = QEstoque.estoque;

        builder.and(qEstoque.ativo.eq(true));

        Optional.ofNullable(filter.id())
                .ifPresent(id -> builder.and(qEstoque.id.eq(id)));

        Optional.ofNullable(filter.produtoId())
                .ifPresent(produtoId -> builder.and(qEstoque.produto.id.eq(produtoId)));

        Optional.ofNullable(filter.quantidadeInicial())
                .ifPresent(quantidade -> builder.and(qEstoque.quantidadeInicial.eq(quantidade)));

        Optional.ofNullable(filter.quantidadeAtual())
                .ifPresent(quantidade -> builder.and(qEstoque.quantidadeAtual.eq(quantidade)));

        var consulta = query.select(Projections.constructor(
                EstoqueProjection.class,
                qEstoque.id,
                qEstoque.produto.id,
                qEstoque.quantidadeInicial,
                qEstoque.quantidadeAtual
        ))
                .from(qEstoque)
                .where(builder)
                .orderBy(qEstoque.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(consulta,pageable,this::buscarQuantidadeDeEstoque);
    }

    private Long buscarQuantidadeDeEstoque(){
        var qEstoque = QEstoque.estoque;

        return query
                .select(qEstoque.id.countDistinct())
                .from(qEstoque)
                .fetchOne();
    }

    @Override
    public Optional<Estoque> buscarEstoqueExistente(Long estoqueId) {

        var qEstoque = QEstoque.estoque;

        var consulta = query
                .selectFrom(qEstoque)
                .where(
                        qEstoque.id.eq(estoqueId),
                        qEstoque.ativo.isTrue()
                )
                .fetchOne();

        return Optional.ofNullable(consulta);
    }
}

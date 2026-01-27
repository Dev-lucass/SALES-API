package com.example.SalesHub.repository.customImpl;

import com.example.SalesHub.dto.filter.ProdutoFilter;
import com.example.SalesHub.dto.projection.ProdutoProjection;
import com.example.SalesHub.model.Produto;
import com.example.SalesHub.model.QProduto;
import com.example.SalesHub.repository.custom.CustomProdutoRepository;
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
public class ProdutoRepositoryImpl implements CustomProdutoRepository {

    @Autowired
    private JPAQueryFactory query;

    @Override
    public Optional<Produto> buscarProdutoDuplicado(Produto produto) {

        var builder = new BooleanBuilder();
        var qProduto = QProduto.produto;

        builder.and(qProduto.nome.eq(produto.getNome()));

        if (produto.getId() != null) {
            builder.and(qProduto.id.notIn(produto.getId()));
        }

        var consulta = query
                .selectFrom(qProduto)
                .where(builder)
                .fetchOne();

        return Optional.ofNullable(consulta);
    }

    @Override
    public Page<ProdutoProjection> buscarProdutos(ProdutoFilter filter, Pageable pageable) {

        var builder = new BooleanBuilder();
        var qProduto = QProduto.produto;

        builder.and(qProduto.ativo.eq(true));

        Optional.ofNullable(filter.id())
                .ifPresent(id -> builder.and(qProduto.id.eq(id)));

        Optional.ofNullable(filter.nome())
                .filter(nome -> !nome.isBlank())
                .ifPresent(nome -> builder.and(qProduto.nome.containsIgnoreCase(nome)));

        Optional.ofNullable(filter.descricao())
                .filter(descricao -> !descricao.isBlank())
                .ifPresent(descricao -> builder.and(qProduto.descricao.containsIgnoreCase(descricao)));

        Optional.ofNullable(filter.preco())
                .ifPresent(preco -> builder.and(qProduto.preco.eq(preco)));

        Optional.ofNullable(filter.dataInicial())
                .ifPresent(inicio -> builder.and(qProduto.criadoEm.goe(inicio.atStartOfDay())));

        Optional.ofNullable(filter.dataFinal())
                .ifPresent(fim -> builder.and(qProduto.criadoEm.loe(fim.atTime(LocalTime.MAX))));

        var consulta = query
                .select(Projections.constructor(
                        ProdutoProjection.class,
                        qProduto.id,
                        qProduto.nome,
                        qProduto.descricao,
                        qProduto.preco,
                        qProduto.criadoEm
                ))
                .from(qProduto)
                .where(builder)
                .orderBy(qProduto.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(consulta, pageable, this::buscarQuantidadeDeProdutos);
    }

    private Long buscarQuantidadeDeProdutos() {
        var qProduto = QProduto.produto;

        return query
                .select(qProduto.id.countDistinct())
                .from(qProduto)
                .fetchOne();
    }

    @Override
    public Optional<Produto> buscarProdutoExistente(Long produtoId) {

        var qProduto = QProduto.produto;

        var consulta = query
                .selectFrom(qProduto)
                .where(
                        qProduto.id.eq(produtoId),
                        qProduto.ativo.isTrue()
                )
                .fetchOne();

        return Optional.ofNullable(consulta);
    }
}

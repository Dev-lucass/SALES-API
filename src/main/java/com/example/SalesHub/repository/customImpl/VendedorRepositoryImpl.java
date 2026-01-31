package com.example.SalesHub.repository.customImpl;

import com.example.SalesHub.dto.filter.VendedorFilter;
import com.example.SalesHub.dto.projection.UsuarioProjection;
import com.example.SalesHub.dto.projection.VendedorProjection;
import com.example.SalesHub.dto.request.VendedorReativacaoRequest;
import com.example.SalesHub.model.QVendedor;
import com.example.SalesHub.model.Vendedor;
import com.example.SalesHub.repository.custom.CustomVendedorRepository;
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
public class VendedorRepositoryImpl implements CustomVendedorRepository {

    @Autowired
    private JPAQueryFactory query;

    @Override
    public Optional<Vendedor> buscarVendedorDuplicado(Vendedor vendedor) {

        var builder = new BooleanBuilder();
        var qVendedor = QVendedor.vendedor;

        builder.and(qVendedor.usuario.eq(vendedor.getUsuario()));

        if (vendedor.getId() != null) {
            builder.and(qVendedor.id.ne(vendedor.getId()));
        }

        var consulta = query
                .selectFrom(qVendedor)
                .where(builder)
                .fetchFirst();

        return Optional.ofNullable(consulta);
    }

    @Override
    public Page<VendedorProjection> buscarVendedores(VendedorFilter filter, Pageable pageable) {

        var builder = new BooleanBuilder();
        var qVendedor = QVendedor.vendedor;

        builder.and(qVendedor.ativo.isTrue());

        Optional.ofNullable(filter.id())
                .ifPresent(vendedorId -> builder.and(qVendedor.id.eq(vendedorId)));

        Optional.ofNullable(filter.usuarioId())
                .ifPresent(usuarioId -> builder.and(qVendedor.usuario.id.eq(usuarioId)));

        Optional.ofNullable(filter.dataNascimento())
                .ifPresent(data -> builder.and(qVendedor.dataNascimento.eq(data)));

        Optional.ofNullable(filter.dataInicial())
                .ifPresent(inicio -> builder.and(qVendedor.criadoEm.goe(inicio.atStartOfDay())));

        Optional.ofNullable(filter.dataFinal())
                .ifPresent(fim -> builder.and(qVendedor.criadoEm.loe(fim.atTime(LocalTime.MAX))));

        var consulta = query
                .select(Projections.constructor(VendedorProjection.class,
                        qVendedor.id,
                        Projections.constructor(UsuarioProjection.class,
                                qVendedor.usuario.id,
                                qVendedor.usuario.nome,
                                qVendedor.usuario.email,
                                qVendedor.usuario.funcao,
                                qVendedor.usuario.criadoEm
                        ),
                        qVendedor.dataNascimento,
                        qVendedor.criadoEm
                ))
                .from(qVendedor)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qVendedor.id.asc())
                .fetch();

        return PageableExecutionUtils.getPage(consulta, pageable, this::buscarQuantidadeDeVendedores);
    }

    private Long buscarQuantidadeDeVendedores() {

        var qVendedor = QVendedor.vendedor;

        return query
                .select(qVendedor.id.countDistinct())
                .from(qVendedor)
                .where(qVendedor.ativo.isTrue())
                .fetchFirst();
    }

    @Override
    public Optional<Vendedor> buscarVendedorExistente(Long vendedorId) {

        var qVendedor = QVendedor.vendedor;

        var consulta = query
                .selectFrom(qVendedor)
                .where(
                        qVendedor.id.eq(vendedorId),
                        qVendedor.ativo.isTrue()
                )
                .fetchFirst();

        return Optional.ofNullable(consulta);
    }

    @Override
    public Optional<Vendedor> reativarContaVendedor(VendedorReativacaoRequest request) {

        var builder = new BooleanBuilder();
        var qVendedor = QVendedor.vendedor;

        builder.and(qVendedor.ativo.isFalse());
        builder.and(qVendedor.id.eq(request.vendedorId()));
        builder.and(qVendedor.cpf.eq(request.cpf()));

        var consulta = query
                .selectFrom(qVendedor)
                .where(builder)
                .fetchFirst();

        return Optional.ofNullable(consulta);
    }
}

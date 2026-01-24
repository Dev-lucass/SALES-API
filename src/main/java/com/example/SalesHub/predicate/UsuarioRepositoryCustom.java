package com.example.SalesHub.predicate;

import com.example.SalesHub.dto.filter.UsuarioFilter;
import com.example.SalesHub.dto.projection.UsuarioProjection;
import com.example.SalesHub.model.QUsuario;
import com.example.SalesHub.model.Usuario;
import com.example.SalesHub.repository.CustomUsuarioRepository;
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
public class UsuarioRepositoryCustom implements CustomUsuarioRepository {

    @Autowired
    private JPAQueryFactory query;

    @Override
    public Optional<Usuario> buscarUsuarioDuplicado(Usuario usuario) {

        var builder = new BooleanBuilder();
        var qUsuario = QUsuario.usuario;

        builder.or(qUsuario.nome.eq(usuario.getNome()))
                .or(qUsuario.email.eq(usuario.getEmail()));

        if (usuario.getId() != null) {
            builder.and(qUsuario.id.eq(usuario.getId()));
        }

        var consulta = query
                .selectFrom(qUsuario)
                .where(builder)
                .fetchOne();

        return Optional.ofNullable(consulta);
    }

    @Override
    public Page<UsuarioProjection> buscarUsuarios(UsuarioFilter filter, Pageable pageable) {

        var buillder = new BooleanBuilder();
        var qUsuario = QUsuario.usuario;

        buillder.and(qUsuario.ativo.eq(true));

        Optional.ofNullable(filter.id())
                .ifPresent(id -> buillder.and(qUsuario.id.eq(id)));

        Optional.ofNullable(filter.nome())
                .filter(nome -> !nome.isBlank())
                .ifPresent(nome -> buillder.and(qUsuario.nome.startsWithIgnoreCase(nome)));

        Optional.ofNullable(filter.email())
                .filter(email -> !email.isBlank())
                .ifPresent(email -> buillder.and(qUsuario.email.startsWithIgnoreCase(email)));

        Optional.ofNullable(filter.dataInicial())
                .ifPresent(data -> buillder.and(qUsuario.criadoEm.goe(data.atStartOfDay())));

        Optional.ofNullable(filter.dataFinal())
                .ifPresent(data -> buillder.and(qUsuario.criadoEm.loe(data.atTime(LocalTime.MAX))));

        var consulta = query
                .select(Projections.constructor(UsuarioProjection.class,
                        qUsuario.id,
                        qUsuario.nome,
                        qUsuario.email,
                        qUsuario.criadoEm
                ))
                .from(qUsuario)
                .where(buillder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(consulta, pageable, this::buscarQuantidadeTotalDeUsuarios);
    }

    private Long buscarQuantidadeTotalDeUsuarios() {

        var qUsuario = QUsuario.usuario;

        return query
                .select(qUsuario.id.countDistinct())
                .from(qUsuario)
                .fetchOne();
    }

    @Override
    public Optional<Usuario> buscarUsuarioExistente(Long usuarioId) {

        var qUsuario = QUsuario.usuario;

        var consulta = query
                .selectFrom(qUsuario)
                .where(qUsuario.id.eq(usuarioId))
                .fetchOne();

        return Optional.ofNullable(consulta);
    }

}

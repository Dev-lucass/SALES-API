package com.example.SalesHub.repository.jpa;

import com.example.SalesHub.dto.projection.MetricaProjection;
import com.example.SalesHub.model.Historico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import java.util.List;

public interface HistoricoRepository extends JpaRepository<Historico, Long>, QuerydslPredicateExecutor<Historico> {

    @NativeQuery("""
             SELECT\s
                 ROW_NUMBER() OVER(ORDER BY SUM(h.quantidaderetirada) DESC) AS rank,
                 p.id AS produtoId,
                 p.nome AS produto,
                 SUM(h.quantidaderetirada) AS quantidadeRetirada
             FROM historico h
             JOIN produto p ON h.produto = p.id
             GROUP BY p.id, p.nome
             ORDER BY quantidadeRetirada DESC
             LIMIT 10
           \s""")
    List<MetricaProjection> buscarMetricas();
}

package com.example.SalesHub.repository.jpa;

import com.example.SalesHub.model.Historico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface HistoricoRepository extends JpaRepository<Historico,Long>, QuerydslPredicateExecutor<Historico> {}

package com.example.SalesHub.repository.jpa;

import com.example.SalesHub.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface EstoqueRepository extends JpaRepository<Estoque,Long>, QuerydslPredicateExecutor<Estoque> {}

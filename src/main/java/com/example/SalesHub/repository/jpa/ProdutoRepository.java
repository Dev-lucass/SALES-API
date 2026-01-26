package com.example.SalesHub.repository.jpa;

import com.example.SalesHub.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ProdutoRepository extends JpaRepository<Produto,Long>, QuerydslPredicateExecutor<Produto> {}

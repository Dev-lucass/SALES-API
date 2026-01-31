package com.example.SalesHub.repository.jpa;

import com.example.SalesHub.model.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface VendedorRepository extends JpaRepository<Vendedor,Long>, QuerydslPredicateExecutor<Vendedor> {}

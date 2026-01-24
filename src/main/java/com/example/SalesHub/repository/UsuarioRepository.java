package com.example.SalesHub.repository;

import com.example.SalesHub.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface UsuarioRepository extends JpaRepository<Usuario,Long>, QuerydslPredicateExecutor<Usuario> {}

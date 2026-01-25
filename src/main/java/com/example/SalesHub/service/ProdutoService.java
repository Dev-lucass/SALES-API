package com.example.SalesHub.service;

import com.example.SalesHub.mapper.ProdutoMapper;
import com.example.SalesHub.repository.jpa.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;
    private final ProdutoMapper mapper;

}

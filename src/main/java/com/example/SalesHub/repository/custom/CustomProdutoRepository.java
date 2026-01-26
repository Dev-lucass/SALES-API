package com.example.SalesHub.repository.custom;

import com.example.SalesHub.dto.filter.ProdutoFilter;
import com.example.SalesHub.dto.projection.ProdutoProjection;
import com.example.SalesHub.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface CustomProdutoRepository {
    Optional<Produto> buscarProdutoDuplicado(Produto produto);
    Page<ProdutoProjection> buscarProdutos(ProdutoFilter filter, Pageable pageable);
    Optional<Produto> buscarProdutoExistente(Long produtoId);
}

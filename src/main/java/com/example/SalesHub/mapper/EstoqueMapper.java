package com.example.SalesHub.mapper;

import com.example.SalesHub.dto.request.EstoqueRequest;
import com.example.SalesHub.dto.response.entity.EstoqueResponse;
import com.example.SalesHub.model.Estoque;
import com.example.SalesHub.model.Produto;
import org.springframework.stereotype.Component;

@Component
public class EstoqueMapper {

    public Estoque toEntity(EstoqueRequest request, Produto produto){
        return Estoque.builder()
                .produto(produto)
                .quantidadeInicial(request.quantidade())
                .quantidadeAtual(request.quantidade())
                .build();
    }

    public EstoqueResponse toResponse(Estoque estoque){
        return EstoqueResponse.builder()
                .id(estoque.getId())
                .produtoId(estoque.getProduto().getId())
                .quantidadeInicial(estoque.getQuantidadeInicial())
                .quantidadeAtual(estoque.getQuantidadeAtual())
                .build();
    }
}

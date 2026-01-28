package com.example.SalesHub.mapper;

import com.example.SalesHub.dto.request.VendaRequest;
import com.example.SalesHub.dto.response.entity.EstoqueResponse;
import com.example.SalesHub.dto.response.entity.ProdutoResponse;
import com.example.SalesHub.dto.response.entity.UsuarioResponse;
import com.example.SalesHub.dto.response.entity.VendaResponse;
import com.example.SalesHub.model.Produto;
import com.example.SalesHub.model.Usuario;
import com.example.SalesHub.model.Venda;
import com.example.SalesHub.model.enums.StatusPagamento;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class VendaMapper {

    public Venda toEntity(VendaRequest request, Usuario usuario, Produto produto) {
        return Venda.builder()
                .usuario(usuario)
                .valor(request.preco())
                .produto(produto)
                .statusPagamento(StatusPagamento.PENDENTE)
                .dataVenda(LocalDateTime.now())
                .build();
    }

    public VendaResponse toReponse(Venda venda, UsuarioResponse usuarioResponse, ProdutoResponse produtoResponse, EstoqueResponse estoqueResponse) {
        return VendaResponse.builder()
                .id(venda.getId())
                .usuario(usuarioResponse)
                .produto(produtoResponse)
                .estoque(estoqueResponse)
                .statusPagamento(venda.getStatusPagamento())
                .dataVenda(venda.getDataVenda())
                .build();
    }
}

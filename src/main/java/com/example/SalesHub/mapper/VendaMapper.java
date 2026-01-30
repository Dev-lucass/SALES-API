package com.example.SalesHub.mapper;

import com.example.SalesHub.dto.request.VendaRequest;
import com.example.SalesHub.dto.response.entity.EstoqueResponse;
import com.example.SalesHub.dto.response.entity.ProdutoResponse;
import com.example.SalesHub.dto.response.entity.UsuarioResponse;
import com.example.SalesHub.dto.response.entity.VendaResponse;
import com.example.SalesHub.model.Produto;
import com.example.SalesHub.model.Usuario;
import com.example.SalesHub.model.Venda;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class VendaMapper {

    public Venda toEntity(VendaRequest request, Usuario usuario, Produto produto) {
        return Venda.builder()
                .usuario(usuario)
                .valor(request.valor())
                .produto(produto)
                .valor(request.valor())
                .quantidade(request.quantidade())
                .dataVenda(LocalDateTime.now())
                .build();
    }

    public VendaResponse toReponse(Venda venda, UsuarioResponse usuarioResponse, ProdutoResponse produtoResponse, EstoqueResponse estoqueResponse) {
        return VendaResponse.builder()
                .id(venda.getId())
                .usuario(usuarioResponse)
                .produto(produtoResponse)
                .estoque(estoqueResponse)
                .valor(venda.getValor())
                .quantidade(venda.getQuantidade())
                .valorTotalVendas(venda.getValorTotalVendas())
                .dataVenda(venda.getDataVenda())
                .build();
    }
}

package com.example.SalesHub.mapper;

import com.example.SalesHub.dto.response.entity.EstoqueResponse;
import com.example.SalesHub.dto.response.entity.HistoricoResponse;
import com.example.SalesHub.dto.response.entity.ProdutoResponse;
import com.example.SalesHub.dto.response.entity.UsuarioResponse;
import com.example.SalesHub.model.Estoque;
import com.example.SalesHub.model.Historico;
import com.example.SalesHub.model.Produto;
import com.example.SalesHub.model.Usuario;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class HistoricoMapper {

    public Historico toEntity(Usuario usuario, Produto produto, Estoque estoque)
    {
        return Historico.builder()
                .usuario(usuario)
                .produto(produto)
                .estoque(estoque)
                .criadoEm(LocalDateTime.now())
                .build();
    }

    public HistoricoResponse toResponse(Historico historico, UsuarioResponse usuario, ProdutoResponse produto, EstoqueResponse estoque)
    {
        return HistoricoResponse.builder()
                .id(historico.getId())
                .usuario(usuario)
                .produto(produto)
                .estoque(estoque)
                .criadoEm(historico.getCriadoEm())
                .build();
    }
}

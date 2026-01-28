package com.example.SalesHub.dto.response.entity;

import com.example.SalesHub.model.enums.StatusPagamento;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record VendaResponse(
        Long id,
        UsuarioResponse usuario,
        ProdutoResponse produto,
        EstoqueResponse estoque,
        BigDecimal valor,
        Long quantidade,
        StatusPagamento statusPagamento,
        LocalDateTime dataVenda) {}

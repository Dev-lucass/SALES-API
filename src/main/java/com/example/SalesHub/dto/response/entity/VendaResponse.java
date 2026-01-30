package com.example.SalesHub.dto.response.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
        BigDecimal valorTotalVendas,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime dataVenda) {}

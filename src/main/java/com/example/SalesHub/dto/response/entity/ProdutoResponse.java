package com.example.SalesHub.dto.response.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record ProdutoResponse(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime criadoEm) {}

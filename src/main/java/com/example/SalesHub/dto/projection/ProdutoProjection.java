package com.example.SalesHub.dto.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record ProdutoProjection(
        Long id,
        String nome,
        String descricao,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime criadoEm) {}

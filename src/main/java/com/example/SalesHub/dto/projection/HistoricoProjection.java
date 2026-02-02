package com.example.SalesHub.dto.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record HistoricoProjection(
        Long id,
        Long usuarioId,
        Long produtoId,
        Long estoqueId,
        BigDecimal quantidadeRetirada,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime criadoEm) {}

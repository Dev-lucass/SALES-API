package com.example.SalesHub.dto.projection;

import com.example.SalesHub.model.enums.StatusPagamento;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record VendaProjection(
        Long id,
        Long usuarioId,
        String usuario,
        BigDecimal valor,
        StatusPagamento statusPagamento,
        LocalDateTime dataVenda) {}

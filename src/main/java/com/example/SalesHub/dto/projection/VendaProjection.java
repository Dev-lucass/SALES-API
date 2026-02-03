package com.example.SalesHub.dto.projection;

import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record VendaProjection(
        Long id,
        Long usuarioId,
        String usuario,
        BigDecimal valor,
        BigDecimal quantidade,
        BigDecimal valorTotalVendas,
        LocalDateTime dataVenda) {}
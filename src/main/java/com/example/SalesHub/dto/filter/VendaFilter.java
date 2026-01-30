package com.example.SalesHub.dto.filter;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record VendaFilter(
        Long id,
        Long usuarioId,
        Long valor,
        Long quantidade,
        BigDecimal valorTotalVendas,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataInicial,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataFinal) {}

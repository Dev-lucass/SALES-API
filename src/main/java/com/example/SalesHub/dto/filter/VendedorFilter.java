package com.example.SalesHub.dto.filter;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import java.time.LocalDate;

@Builder
public record VendedorFilter(
    Long id,
    Long usuarioId,
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate dataNascimento,
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate dataInicial,
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate dataFinal) {}

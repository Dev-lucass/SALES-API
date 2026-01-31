package com.example.SalesHub.dto.filter;

import com.example.SalesHub.model.enums.Funcao;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import java.time.LocalDate;

@Builder
public record UsuarioFilter(
        Long id,
        String nome,
        String email,
        Funcao funcao,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataInicial,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataFinal) {}

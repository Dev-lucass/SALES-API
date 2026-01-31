package com.example.SalesHub.dto.projection;

import com.example.SalesHub.dto.response.entity.UsuarioResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record VendedorProjection(
    Long id,
    UsuarioResponse usuario,
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate dataNascimento,
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime criadoEm) {}

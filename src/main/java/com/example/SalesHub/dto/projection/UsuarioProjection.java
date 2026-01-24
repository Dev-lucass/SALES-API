package com.example.SalesHub.dto.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record UsuarioProjection(
        Long id,
        String nome,
        String email,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime criadoEm) {}

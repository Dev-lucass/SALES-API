package com.example.SalesHub.dto.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record HistoricoProjection(
  Long id,
  UsuarioProjection usuario,
  ProdutoProjection produto,
  EstoqueProjection estoque,
  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
  LocalDateTime criadoEm) {}

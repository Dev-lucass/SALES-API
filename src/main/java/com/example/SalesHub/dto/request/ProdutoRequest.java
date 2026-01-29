package com.example.SalesHub.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ProdutoRequest(
        @NotBlank @Size(min = 2, max = 250)
        String nome,
        @NotBlank @Size(min = 2, max = 300)
        String descricao) {}

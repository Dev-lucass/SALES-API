package com.example.SalesHub.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record LoginRequest(
        @NotBlank @Size(min = 1, max = 150)
        String nome,
        @NotBlank @Size(min = 1, max = 250)
        String senha){}

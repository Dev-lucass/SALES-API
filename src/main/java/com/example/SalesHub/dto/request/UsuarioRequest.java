package com.example.SalesHub.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UsuarioRequest(
                            @NotBlank @Size(min = 2, max = 150)
                             String nome,
                             @NotBlank @Email @Size(min = 2, max = 250)
                             String email,
                             @NotBlank @Size(min = 2, max = 250)
                             String senha) {}

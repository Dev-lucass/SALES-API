package com.example.SalesHub.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.br.CPF;

@Builder
public record VendedorReativacaoRequest(
        @NotNull @Min(1)
        Long vendedorId,
        @NotBlank @CPF
        String cpf) {}

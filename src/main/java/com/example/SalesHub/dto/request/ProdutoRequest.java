package com.example.SalesHub.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record ProdutoRequest(
                            @NotBlank @Size(min = 2, max = 250)
                             String nome,
                             @NotBlank @Size(min = 2, max = 300)
                             String descricao,
                             @NotNull @Min(value = 0)
                             BigDecimal preco) {}

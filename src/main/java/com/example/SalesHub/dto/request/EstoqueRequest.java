package com.example.SalesHub.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record EstoqueRequest(@Min(1) @NotNull
                                                           Long produtoId,
                                                           @Min(1) @NotNull
                                                           Long quantidade) {}

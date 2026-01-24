package com.example.SalesHub.dto.response.error;

import lombok.Builder;

@Builder
public record CamposInvalidosError(String erro, String campo) {}

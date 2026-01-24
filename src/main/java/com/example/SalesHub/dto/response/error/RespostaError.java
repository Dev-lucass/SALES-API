package com.example.SalesHub.dto.response.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record RespostaError(int status,
                            String erro,
                            @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                            LocalDateTime data) {}

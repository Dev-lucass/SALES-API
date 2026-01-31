package com.example.SalesHub.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Builder;
import org.hibernate.validator.constraints.br.CPF;
import java.time.LocalDate;

@Builder
public record VendedorRequest(
        @NotNull @Min(1)
        Long usuarioId,
        @NotBlank @CPF
        String cpf,
        @NotNull  @Past @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataNascimento) {}

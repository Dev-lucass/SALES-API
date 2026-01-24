package com.example.SalesHub.dto.response.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CamposInvalidosRespostaError(int status,
                                           String erro,
                                           List<CamposInvalidosError> camposInvalidos,
                                           @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                                           LocalDateTime data)
{
     public static  List<CamposInvalidosError> camposInvalidos(MethodArgumentNotValidException ex){
         return  ex.getFieldErrors()
                 .stream()
                 .map(errors -> CamposInvalidosError.builder()
                         .erro(errors.getDefaultMessage())
                         .campo(errors.getField()).build())
                 .toList();
     }
}

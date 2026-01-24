package com.example.SalesHub.globalHandler;

import com.example.SalesHub.dto.response.error.CamposInvalidosRespostaError;
import com.example.SalesHub.dto.response.error.RespostaError;
import com.example.SalesHub.exception.UsuarioDuplicadoException;
import com.example.SalesHub.exception.UsuarioNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CamposInvalidosRespostaError camposInvalidos(MethodArgumentNotValidException ex) {
        return CamposInvalidosRespostaError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .erro("Erro de validação de dados")
                .camposInvalidos(CamposInvalidosRespostaError.camposInvalidos(ex))
                .data(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(UsuarioDuplicadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public RespostaError usuarioDuplicado(UsuarioDuplicadoException ex) {
        return RespostaError.builder()
                .status(HttpStatus.CONFLICT.value())
                .erro(ex.getMessage())
                .data(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RespostaError usuarioNaoEncontrado(UsuarioNaoEncontradoException ex) {
        return RespostaError.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .erro(ex.getMessage())
                .data(LocalDateTime.now())
                .build();
    }
}

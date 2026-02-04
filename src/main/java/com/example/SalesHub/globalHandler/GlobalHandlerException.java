package com.example.SalesHub.globalHandler;

import com.example.SalesHub.dto.response.error.CamposInvalidosRespostaError;
import com.example.SalesHub.dto.response.error.RespostaError;
import com.example.SalesHub.exception.EntidadeDuplicadaException;
import com.example.SalesHub.exception.EntidadeNaoEncontradaException;
import com.example.SalesHub.exception.FuncaoInvalidaException;
import com.example.SalesHub.exception.QuantidadeIndiposnivelException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
    @ExceptionHandler(EntidadeDuplicadaException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public RespostaError usuarioDuplicado(EntidadeDuplicadaException ex) {
        return RespostaError.builder()
                .status(HttpStatus.CONFLICT.value())
                .erro(ex.getMessage())
                .data(LocalDateTime.now())
                .build();
    }
    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RespostaError usuarioNaoEncontrado(EntidadeNaoEncontradaException ex) {
        return RespostaError.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .erro(ex.getMessage())
                .data(LocalDateTime.now())
                .build();
    }
    @ExceptionHandler(QuantidadeIndiposnivelException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public RespostaError quantidadeIndisponivel(QuantidadeIndiposnivelException ex) {
        return RespostaError.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .erro(ex.getMessage())
                .data(LocalDateTime.now())
                .build();
    }
    @ExceptionHandler(FuncaoInvalidaException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RespostaError funcaoInvalida(FuncaoInvalidaException ex) {
        return RespostaError.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .erro(ex.getMessage())
                .data(LocalDateTime.now())
                .build();
    }
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public RespostaError erroGenerico(Exception ex) {
//        return RespostaError.builder()
//                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                .erro("Ocorreu um erro interno inesperado. Nossa equipe já foi notificada.")
//                .data(LocalDateTime.now())
//                .build();
//    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RespostaError erroFormatoJson(HttpMessageNotReadableException ex) {
        return RespostaError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .erro("Corpo da requisição malformado ou erro de sintaxe no JSON.")
                .data(LocalDateTime.now())
                .build();
    }
}
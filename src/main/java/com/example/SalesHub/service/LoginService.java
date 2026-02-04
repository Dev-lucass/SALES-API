package com.example.SalesHub.service;

import com.example.SalesHub.configuration.CustomUserDetailsConfig;
import com.example.SalesHub.configuration.JwtConfig;
import com.example.SalesHub.dto.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager manager;
    private final JwtConfig jwtConfig;

    public String logar(LoginRequest request) {

        var dadosLogin = new UsernamePasswordAuthenticationToken(
                request.nome(),
                request.senha()
        );

        var autenticado = manager.authenticate(
                dadosLogin
        );

        var userDetailsConfig = (CustomUserDetailsConfig) autenticado.getPrincipal();

        return jwtConfig.gerar(userDetailsConfig.usuario());
    }
}
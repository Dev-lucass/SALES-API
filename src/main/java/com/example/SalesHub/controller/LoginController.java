package com.example.SalesHub.controller;

import com.example.SalesHub.dto.request.LoginRequest;
import com.example.SalesHub.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService service;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String logar(@RequestBody @Valid LoginRequest request) {
        return service.logar(request);
    }
}
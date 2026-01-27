package com.example.SalesHub.controller;

import com.example.SalesHub.dto.filter.UsuarioFilter;
import com.example.SalesHub.dto.projection.UsuarioProjection;
import com.example.SalesHub.dto.request.UsuarioRequest;
import com.example.SalesHub.dto.response.entity.UsuarioResponse;
import com.example.SalesHub.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse salvar(@RequestBody @Valid UsuarioRequest request) {
        return service.salvar(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<UsuarioProjection> buscar(@ModelAttribute UsuarioFilter filter, @PageableDefault Pageable pageable) {
        return service.buscar(filter, pageable);
    }

    @PutMapping("{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Long usuarioId, @RequestBody @Valid UsuarioRequest request) {
        service.atualizar(usuarioId, request);
    }

    @DeleteMapping("{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable Long usuarioId) {
        service.desativar(usuarioId);
    }
}

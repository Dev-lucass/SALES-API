package com.example.SalesHub.controller;

import com.example.SalesHub.dto.filter.VendedorFilter;
import com.example.SalesHub.dto.projection.VendedorProjection;
import com.example.SalesHub.dto.request.VendedorReativacaoRequest;
import com.example.SalesHub.dto.request.VendedorRequest;
import com.example.SalesHub.dto.response.entity.VendedorReponse;
import com.example.SalesHub.service.VendedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/vendedor")
@RequiredArgsConstructor
public class VendedorController {

    private final VendedorService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public VendedorReponse salvar(@RequestBody @Valid VendedorRequest request) {
        return service.salvar(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public Page<VendedorProjection> buscar(@ModelAttribute VendedorFilter filter, @PageableDefault Pageable pageable) {
        return service.buscar(filter, pageable);
    }

    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @DeleteMapping("{vendedorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Long vendedorId) {
        service.desativar(vendedorId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reativarConta(@RequestBody @Valid VendedorReativacaoRequest request) {
        service.reativar(request);
    }
}
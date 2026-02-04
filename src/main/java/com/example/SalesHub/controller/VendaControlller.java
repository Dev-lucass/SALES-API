package com.example.SalesHub.controller;

import com.example.SalesHub.dto.filter.VendaFilter;
import com.example.SalesHub.dto.projection.VendaProjection;
import com.example.SalesHub.dto.request.VendaRequest;
import com.example.SalesHub.dto.response.entity.VendaResponse;
import com.example.SalesHub.service.VendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/venda")
@RequiredArgsConstructor
public class VendaControlller {

    private final VendaService service;

    @PreAuthorize("hasAnyRole('VENDEDOR','ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VendaResponse salvar(@RequestBody @Valid VendaRequest request) {
        return service.salvar(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<VendaProjection> buscar(@ModelAttribute VendaFilter filter, @PageableDefault Pageable pageable) {
        return service.buscar(filter, pageable);
    }
}

package com.example.SalesHub.controller;

import com.example.SalesHub.dto.filter.HistoricoFilter;
import com.example.SalesHub.dto.projection.HistoricoProjection;
import com.example.SalesHub.service.HistoricoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/historico")
@RequiredArgsConstructor
public class HistoricoController {

    private final HistoricoService service;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<HistoricoProjection> buscar(@ModelAttribute HistoricoFilter filter, @PageableDefault Pageable pageable) {
        return service.buscar(
                filter,
                pageable
        );
    }
}

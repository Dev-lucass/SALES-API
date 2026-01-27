package com.example.SalesHub.controller;

import com.example.SalesHub.dto.filter.EstoqueFilter;
import com.example.SalesHub.dto.projection.EstoqueProjection;
import com.example.SalesHub.dto.request.EstoqueRequest;
import com.example.SalesHub.dto.response.entity.EstoqueResponse;
import com.example.SalesHub.service.EstoqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/estoque")
@RequiredArgsConstructor
public class EstoqueController {

    private final EstoqueService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EstoqueResponse salvar(@RequestBody @Valid EstoqueRequest request){
        return service.salvar(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<EstoqueProjection> buscar(@ModelAttribute EstoqueFilter filter, @PageableDefault Pageable pageable){
        return service.buscar(filter,pageable);
    }

    @PutMapping("{estoqueId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Long estoqueId, @RequestBody @Valid EstoqueRequest request){
        service.atualizar(estoqueId, request);
    }

    @DeleteMapping("{estoqueId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable Long estoqueId){
        service.desativar(estoqueId);
    }
}
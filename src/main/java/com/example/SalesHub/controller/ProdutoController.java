package com.example.SalesHub.controller;

import com.example.SalesHub.dto.filter.ProdutoFilter;
import com.example.SalesHub.dto.projection.ProdutoProjection;
import com.example.SalesHub.dto.request.ProdutoRequest;
import com.example.SalesHub.dto.response.entity.ProdutoResponse;
import com.example.SalesHub.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/produto")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponse salvar(@RequestBody @Valid ProdutoRequest request) {
        return service.salvar(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ProdutoProjection> buscar(@ModelAttribute ProdutoFilter filter, @PageableDefault Pageable pageable) {
        return service.buscar(filter, pageable);
    }

    @PutMapping("produtoId")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ProdutoResponse atualizar(@PathVariable Long produtoId, @RequestBody @Valid ProdutoRequest request) {
        return service.atualizar(produtoId, request);
    }

    @DeleteMapping("produtoId")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable Long produtoId) {
        service.desativar(produtoId);
    }
}

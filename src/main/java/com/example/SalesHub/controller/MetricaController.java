package com.example.SalesHub.controller;

import com.example.SalesHub.dto.projection.MetricaProjection;
import com.example.SalesHub.service.MetricaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("api/v1/metrica")
@RequiredArgsConstructor
public class MetricaController {

    private final MetricaService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MetricaProjection> buscarMetricas() {
        return service.buscarMetricas();
    }
}
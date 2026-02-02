package com.example.SalesHub.service;

import com.example.SalesHub.dto.projection.MetricaProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MetricaService {

    private final HistoricoService service;

    public List<MetricaProjection> buscarMetricas(){
        return service.buscarMetricas();
    }
}

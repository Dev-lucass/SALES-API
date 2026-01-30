package com.example.SalesHub.repository.custom;

import com.example.SalesHub.dto.filter.HistoricoFilter;
import com.example.SalesHub.dto.projection.HistoricoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomHistoricoRepository {
    Page<HistoricoProjection> buscarHistorico(HistoricoFilter filter, Pageable pageable);
}

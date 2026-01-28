package com.example.SalesHub.repository.custom;

import com.example.SalesHub.dto.filter.VendaFilter;
import com.example.SalesHub.dto.projection.VendaProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomVendaRepository {
    Page<VendaProjection> buscarVendas(VendaFilter filter, Pageable pageable);
}

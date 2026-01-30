package com.example.SalesHub.repository.custom;

import com.example.SalesHub.dto.filter.VendaFilter;
import com.example.SalesHub.dto.projection.VendaProjection;
import com.example.SalesHub.model.Venda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface CustomVendaRepository {
    Page<VendaProjection> buscarVendas(VendaFilter filter, Pageable pageable);
    BigDecimal buscarValorTotalDeVendas(Venda venda);
}

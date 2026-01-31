package com.example.SalesHub.repository.custom;

import com.example.SalesHub.dto.filter.VendedorFilter;
import com.example.SalesHub.dto.projection.VendedorProjection;
import com.example.SalesHub.model.Vendedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface CustomVendedorRepository {
    Optional<Vendedor> buscarVendedorDuplicado(Vendedor vendedor);
    Page<VendedorProjection> buscarVendedores(VendedorFilter filter, Pageable pageable);
    Optional<Vendedor> buscarVendedorExistente(Long vendedorId);
}

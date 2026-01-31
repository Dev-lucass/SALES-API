package com.example.SalesHub.repository.custom;

import com.example.SalesHub.dto.filter.UsuarioFilter;
import com.example.SalesHub.dto.projection.VendedorProjection;
import com.example.SalesHub.model.Usuario;
import com.example.SalesHub.model.Vendedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface CustomVendedorRepository {
    Optional<Vendedor> buscarUsuarioDuplicado(Usuario Usuario);
    Page<VendedorProjection> buscarUsuarios(UsuarioFilter filter, Pageable pageable);
    Optional<Vendedor> buscarUsuarioExistente(Long usuarioId);
}

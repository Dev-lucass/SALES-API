package com.example.SalesHub.repository.customImpl;

import com.example.SalesHub.dto.filter.UsuarioFilter;
import com.example.SalesHub.dto.projection.VendedorProjection;
import com.example.SalesHub.model.Usuario;
import com.example.SalesHub.model.Vendedor;
import com.example.SalesHub.repository.custom.CustomVendedorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class VendedorRepositoryImpl implements CustomVendedorRepository {

    @Override
    public Optional<Vendedor> buscarUsuarioDuplicado(Usuario Usuario) {
        return Optional.empty();
    }

    @Override
    public Page<VendedorProjection> buscarUsuarios(UsuarioFilter filter, Pageable pageable) {
        return null;
    }

    @Override
    public Optional<Vendedor> buscarUsuarioExistente(Long usuarioId) {
        return Optional.empty();
    }
}

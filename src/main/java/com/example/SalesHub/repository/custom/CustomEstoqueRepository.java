package com.example.SalesHub.repository.custom;

import com.example.SalesHub.dto.filter.EstoqueFilter;
import com.example.SalesHub.dto.projection.EstoqueProjection;
import com.example.SalesHub.model.Estoque;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface CustomEstoqueRepository {
    Optional<Estoque> buscarEstoqueDuplicado(Estoque estoque);
    Page<EstoqueProjection> buscarEstoques(EstoqueFilter filter, Pageable pageable);
    Optional<Estoque> buscarEstoqueExistente(Long estoqueId);
}

package com.example.SalesHub.service;

import com.example.SalesHub.dto.filter.HistoricoFilter;
import com.example.SalesHub.dto.projection.HistoricoProjection;
import com.example.SalesHub.mapper.HistoricoMapper;
import com.example.SalesHub.model.Estoque;
import com.example.SalesHub.model.Historico;
import com.example.SalesHub.model.Produto;
import com.example.SalesHub.model.Usuario;
import com.example.SalesHub.repository.customImpl.HistoricoRepositoryImpl;
import com.example.SalesHub.repository.jpa.HistoricoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoricoService {

    private final HistoricoMapper mapper;
    private final HistoricoRepository repository;
    private final HistoricoRepositoryImpl customRepository;

    public void salvar(Usuario usuario, Produto produto, Estoque estoque) {

        var historico = mapearEntidade(
                usuario,
                produto,
                estoque
        );

        repository.save(historico);
    }

    public Page<HistoricoProjection> buscar(HistoricoFilter filter, Pageable pageable){
        return customRepository.buscarHistorico(
                filter,
                pageable
        );
    }

    private Historico mapearEntidade(Usuario usuario, Produto produto, Estoque estoque) {
        return mapper.toEntity(
                usuario,
                produto,
                estoque
        );
    }
}
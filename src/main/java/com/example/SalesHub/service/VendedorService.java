package com.example.SalesHub.service;

import com.example.SalesHub.mapper.VendedorMapper;
import com.example.SalesHub.repository.customImpl.VendedorRepositoryImpl;
import com.example.SalesHub.repository.jpa.VendedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendedorService {

    private final VendedorRepository repository;
    private final VendedorRepositoryImpl customRepository;
    private final VendedorMapper mapper;

    // SALVAR

    //BUSCAR

    //ATUALIZAR

    // DESATIVAR --- FAZER ALGO DIFERENTE CASO NAO QUEIRA MAIS SER VENDEDOR SEI LA

}

package com.example.SalesHub.mapper;

import com.example.SalesHub.dto.request.VendedorRequest;
import com.example.SalesHub.dto.response.entity.VendedorReponse;
import com.example.SalesHub.model.Usuario;
import com.example.SalesHub.model.Vendedor;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class VendedorMapper {

    public Vendedor toEntity(VendedorRequest request, Usuario usuario){
        return Vendedor.builder()
                .usuario(usuario)
                .cpf(request.cpf())
                .dataNascimento(request.dataNascimento())
                .criadoEm(LocalDateTime.now())
                .build();
    }

    public VendedorReponse toResponse(Vendedor vendedor){
        return VendedorReponse.builder()
                .id(vendedor.getId())
                .usuario(vendedor.getUsuario())
                .dataNascimento(vendedor.getDataNascimento())
                .criadoEm(vendedor.getCriadoEm())
                .build();
    }
}
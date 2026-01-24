package com.example.SalesHub.mapper;

import com.example.SalesHub.dto.request.UsuarioRequest;
import com.example.SalesHub.dto.response.entity.UsuarioResponse;
import com.example.SalesHub.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequest request) {
        return Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(request.senha())
                .build();
    }

    public UsuarioResponse toResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .criadoEm(usuario.getCriadoEm())
                .build();
    }
}

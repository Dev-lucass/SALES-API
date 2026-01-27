package com.example.SalesHub.service;

import com.example.SalesHub.dto.filter.UsuarioFilter;
import com.example.SalesHub.dto.projection.UsuarioProjection;
import com.example.SalesHub.dto.request.UsuarioRequest;
import com.example.SalesHub.dto.response.entity.UsuarioResponse;
import com.example.SalesHub.exception.EntidadeDuplicadaException;
import com.example.SalesHub.exception.EntidadeNaoEncontradaException;
import com.example.SalesHub.mapper.UsuarioMapper;
import com.example.SalesHub.model.Usuario;
import com.example.SalesHub.repository.customImpl.UsuarioRepositoryImpl;
import com.example.SalesHub.repository.jpa.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioMapper mapper;
    private final UsuarioRepositoryImpl customRepository;
    private final UsuarioRepository repository;

    public UsuarioResponse salvar(UsuarioRequest request) {
        var usuario = mapper.toEntity(request);
        validarDuplicidade(usuario);
        return mapper.toResponse(repository.save(usuario));
    }

    public Page<UsuarioProjection> buscar(UsuarioFilter filter, Pageable pageable) {
        return customRepository.buscarUsuarios(filter, pageable);
    }

    public void atualizar(Long usuarioId, UsuarioRequest request) {
        var usuario = buscarUsuarioExistente(usuarioId);
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(request.senha());

        validarDuplicidade(usuario);

        repository.save(usuario);
    }

    @Transactional
    public void desativar(Long usuarioId) {
        var usuario = buscarUsuarioExistente(usuarioId);
        usuario.setAtivo(false);
    }

    private void validarDuplicidade(Usuario usuario) {
        customRepository.buscarUsuarioDuplicado(usuario)
                .ifPresent(usuarioEncontrado -> {
                    throw new EntidadeDuplicadaException("Usuario ja cadastrado");
                });
    }

    private Usuario buscarUsuarioExistente(Long usuarioId) {
        return customRepository.buscarUsuarioExistente(usuarioId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario não encontrado"));
    }
}

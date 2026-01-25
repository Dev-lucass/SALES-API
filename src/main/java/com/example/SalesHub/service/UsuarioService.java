package com.example.SalesHub.service;

import com.example.SalesHub.dto.filter.UsuarioFilter;
import com.example.SalesHub.dto.projection.UsuarioProjection;
import com.example.SalesHub.dto.request.UsuarioRequest;
import com.example.SalesHub.dto.response.entity.UsuarioResponse;
import com.example.SalesHub.exception.UsuarioDuplicadoException;
import com.example.SalesHub.exception.UsuarioNaoEncontradoException;
import com.example.SalesHub.mapper.UsuarioMapper;
import com.example.SalesHub.model.Usuario;
import com.example.SalesHub.repository.customImpl.UsuarioRepositoryCustom;
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
    private final UsuarioRepositoryCustom customRepository;
    private final UsuarioRepository repository;

    public UsuarioResponse salvar(UsuarioRequest request) {
        var usuario = mapper.toEntity(request);
        validarDuplicidade(usuario);
        return mapper.toResponse(repository.save(usuario));
    }

    public Page<UsuarioProjection> buscar(UsuarioFilter filter, Pageable pageable) {
        return customRepository.buscarUsuarios(filter,pageable);
    }

    public UsuarioResponse atualizar(Long usuarioId, UsuarioRequest request) {

        validarDuplicidade(mapper.toEntity(request));

        var usuario = buscarUsuarioExistente(usuarioId);
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(request.senha());

        return mapper.toResponse(repository.save(usuario));
    }

    @Transactional
    public void desativar(Long usuarioId) {
        var usuario = buscarUsuarioExistente(usuarioId);
        usuario.setAtivo(false);
    }

    private void validarDuplicidade(Usuario usuario) {
        customRepository.buscarUsuarioDuplicado(usuario)
                .ifPresent(usuarioEncontrado -> {
                    throw new UsuarioDuplicadoException("Usuario ja cadastrado");
                });
    }

    private Usuario buscarUsuarioExistente(Long usuarioId) {
        return customRepository.buscarUsuarioExistente(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não encontrado"));
    }
}

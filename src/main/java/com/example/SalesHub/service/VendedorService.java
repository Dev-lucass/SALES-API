package com.example.SalesHub.service;

import com.example.SalesHub.dto.filter.VendedorFilter;
import com.example.SalesHub.dto.projection.VendedorProjection;
import com.example.SalesHub.dto.request.VendedorReativacaoRequest;
import com.example.SalesHub.dto.request.VendedorRequest;
import com.example.SalesHub.dto.response.entity.VendedorReponse;
import com.example.SalesHub.exception.EntidadeDuplicadaException;
import com.example.SalesHub.exception.EntidadeNaoEncontradaException;
import com.example.SalesHub.mapper.VendedorMapper;
import com.example.SalesHub.model.Usuario;
import com.example.SalesHub.model.Vendedor;
import com.example.SalesHub.model.enums.Funcao;
import com.example.SalesHub.repository.customImpl.VendedorRepositoryImpl;
import com.example.SalesHub.repository.jpa.VendedorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendedorService {

    private final VendedorRepository repository;
    private final VendedorRepositoryImpl customRepository;
    private final VendedorMapper mapper;
    private final UsuarioService usuarioService;

    @Transactional
    public VendedorReponse salvar(VendedorRequest request) {

        var usuario = usuarioService.buscarUsuarioExistente(
                request.usuarioId()
        );

        atualizarFuncaoUsuario(
                usuario,
                Funcao.VENDEDOR
        );

        var vendedor = mapper.toEntity(
                request,
                usuario
        );

        validarDuplicidade(vendedor);

        return mapper.toResponse(
                repository.save(vendedor),
                usuarioService.mapearUsuario(usuario)
        );
    }

    public Page<VendedorProjection> buscar(VendedorFilter filter, Pageable pageable) {
        return customRepository.buscarVendedores(
                filter,
                pageable
        );
    }

    public void atualizar(Long vendedorId, VendedorRequest request) {

        var vendedor = buscarVendedorPorId(
                vendedorId
        );

        var usuario = usuarioService.buscarUsuarioExistente(
                request.usuarioId()
        );

        vendedor.setUsuario(
                usuario
        );

        vendedor.setCpf(
                request.cpf()
        );

        vendedor.setDataNascimento(
                request.dataNascimento()
        );

        validarDuplicidade(
                vendedor
        );

        repository.save(vendedor);
    }

    @Transactional
    public void desativar(Long vendedorId){

        var vendedor = buscarVendedorPorId(
                vendedorId
        );

        atualizarFuncaoUsuarioEcontaAtivaVendedor(
                vendedor,
                false,
                Funcao.CLIENTE
        );
    }

    @Transactional
    public void reativar(VendedorReativacaoRequest request) {

        var vendedor = buscarVendedorDesativado(
                request
        );

        atualizarFuncaoUsuarioEcontaAtivaVendedor(
                vendedor,
                true,
                Funcao.VENDEDOR
        );
    }

    private void atualizarFuncaoUsuarioEcontaAtivaVendedor(Vendedor vendedor, Boolean vendedorAtivo, Funcao funcao){
        atualizarAtivoVendedor(
                vendedor,
                vendedorAtivo
        );

        atualizarFuncaoUsuario(
                vendedor.getUsuario(),
                funcao
        );
    }

    private void atualizarFuncaoUsuario(Usuario usuario, Funcao funcao) {
        usuario.setFuncao(funcao);
    }

    private void atualizarAtivoVendedor(Vendedor vendedor, Boolean ativo) {
        vendedor.setAtivo(ativo);
    }

    private void validarDuplicidade(Vendedor vendedor) {
        customRepository.buscarVendedorDuplicado(vendedor)
                .ifPresent(vendedorDuplicado -> {
                    throw new EntidadeDuplicadaException("Vendedor ja cadastrado");
                });
    }

    private Vendedor buscarVendedorDesativado(VendedorReativacaoRequest request) {
        return customRepository.reativarContaVendedor(request)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Credenciais incorretas ou conta esta ativa"));
    }

    private Vendedor buscarVendedorPorId(Long vendedorId) {
        return customRepository.buscarVendedorExistente(vendedorId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Vendedor não encontrado"));
    }
}
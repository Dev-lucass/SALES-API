package com.example.SalesHub.service;

import com.example.SalesHub.dto.filter.ProdutoFilter;
import com.example.SalesHub.dto.projection.ProdutoProjection;
import com.example.SalesHub.dto.request.ProdutoRequest;
import com.example.SalesHub.dto.response.entity.ProdutoResponse;
import com.example.SalesHub.exception.EntidadeDuplicadaException;
import com.example.SalesHub.exception.EntidadeNaoEncontradaException;
import com.example.SalesHub.mapper.ProdutoMapper;
import com.example.SalesHub.model.Produto;
import com.example.SalesHub.repository.customImpl.ProdutoRepositoryCustom;
import com.example.SalesHub.repository.jpa.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;
    private final ProdutoMapper mapper;
    private final ProdutoRepositoryCustom repositoryCustom;

    public ProdutoResponse salvar(ProdutoRequest request) {
        var produto = mapper.toEntity(request);
        validarDuplicidade(produto);
        return mapper.toResponse(repository.save(produto));
    }

    public Page<ProdutoProjection> buscar(ProdutoFilter filter, Pageable pageable) {
        return repositoryCustom.buscarProdutos(filter, pageable);
    }

    public ProdutoResponse atualizar(Long produtoId, ProdutoRequest request) {
        validarDuplicidade(mapper.toEntity(request));

        var produto = buscarProdutoPeloId(produtoId);
        produto.setNome(request.nome());
        produto.setDescricao(request.descricao());
        produto.setPreco(request.preco());

        return mapper.toResponse(produto);
    }

    @Transactional
    public void desativar(Long produtoId) {
        var produto = buscarProdutoPeloId(produtoId);
        produto.setAtivo(false);
    }

    private void validarDuplicidade(Produto produto) {
        repositoryCustom.buscarProdutoDuplicado(produto)
                .ifPresent(produtoDuplicado -> {
                    throw new EntidadeDuplicadaException("Produto ja cadastrado");
                });
    }

    private Produto buscarProdutoPeloId(Long produtoId) {
        return repositoryCustom.buscarProdutoExistente(produtoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto não encontrado"));
    }
}

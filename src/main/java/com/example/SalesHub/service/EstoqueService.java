package com.example.SalesHub.service;

import com.example.SalesHub.dto.filter.EstoqueFilter;
import com.example.SalesHub.dto.projection.EstoqueProjection;
import com.example.SalesHub.dto.request.EstoqueRequest;
import com.example.SalesHub.dto.response.entity.EstoqueResponse;
import com.example.SalesHub.exception.EntidadeDuplicadaException;
import com.example.SalesHub.exception.EntidadeNaoEncontradaException;
import com.example.SalesHub.exception.QuantidadeIndiposnivelException;
import com.example.SalesHub.mapper.EstoqueMapper;
import com.example.SalesHub.model.Estoque;
import com.example.SalesHub.repository.customImpl.EstoqueRepositoryImpl;
import com.example.SalesHub.repository.jpa.EstoqueRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private final EstoqueRepositoryImpl repositoyCustom;
    private final ProdutoService produtoService;
    private final EstoqueRepository repository;
    private final EstoqueMapper mapper;

    public EstoqueResponse salvar(EstoqueRequest request) {

        var produto = produtoService.buscarProdutoPeloId(
                request.produtoId()
        );

        var estoque = mapper.toEntity(
                request, produto
        );

        validarDuplicidade(
                estoque
        );

        return mapper.toResponse(
                repository.save(estoque)
        );
    }

    public Page<EstoqueProjection> buscar(EstoqueFilter filter, Pageable pageable) {
        return repositoyCustom.buscarEstoques(
                filter, pageable
        );
    }

    public void atualizar(Long estoqueId, EstoqueRequest request) {

        var estoque = buscarPorId(
                estoqueId
        );

        var produto = produtoService.buscarProdutoPeloId(
                request.produtoId()
        );

        estoque.setProduto(
                produto
        );

        estoque.setQuantidadeAtual(
                request.quantidade()
        );

        validarDuplicidade(
                estoque
        );

        repository.save(
                estoque
        );
    }

    /**
     * @apiNote so pode desativar se nao estiver em uso, verificar se esta em uso quando criar historico
     */
    @Transactional
    public void desativar(Long estoqueId) {

        var estoque = buscarPorId(
                estoqueId
        );

        estoque.setAtivo(false);
    }

    public EstoqueResponse pegarQuantidadeDoProdutoDoEstoque(Long estoqueId, Long quantidadeParaRetirada) {

        validarQuantidadeDisponivel(
                estoqueId,
                quantidadeParaRetirada
        );

        var estoque = buscarPorId(
                estoqueId
        );

        var quantidadeAtualAtualizada = subtrairQuantidade(
                estoque.getQuantidadeAtual(),
                quantidadeParaRetirada
        );

        estoque.setQuantidadeAtual(
                quantidadeAtualAtualizada
        );

        return mapper.toResponse(
                repository.save(estoque)
        );
    }

    private void validarDuplicidade(Estoque estoque) {
        repositoyCustom.buscarEstoqueDuplicado(estoque)
                .ifPresent(estoqueDuplicado -> {
                            throw new EntidadeDuplicadaException("Produto ja cadastrado no estoque " + estoque.getId());
                        }
                );
    }

    public Estoque buscarPorId(Long estoqueId) {
        return repositoyCustom.buscarEstoqueExistente(estoqueId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Estoque não encontrado"));
    }

    private Long subtrairQuantidade(Long quantidadeAtualEstoque, Long quantidadeRequisitadaParaRetirada) {
        return quantidadeAtualEstoque - quantidadeRequisitadaParaRetirada;
    }

    private void validarQuantidadeDisponivel(Long estoqueId, Long quantidadeParaRetirada) {

        var estoque = buscarPorId(
                estoqueId
        );

        if (estoque.getQuantidadeAtual() <= 0 || estoque.getQuantidadeAtual() - quantidadeParaRetirada <= 0)
            throw new QuantidadeIndiposnivelException("Quantidade do produto indisponivel no estoque " + estoque.getId());
    }
}
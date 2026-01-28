package com.example.SalesHub.service;

import com.example.SalesHub.dto.filter.VendaFilter;
import com.example.SalesHub.dto.projection.VendaProjection;
import com.example.SalesHub.dto.request.VendaRequest;
import com.example.SalesHub.dto.response.entity.VendaResponse;
import com.example.SalesHub.mapper.VendaMapper;
import com.example.SalesHub.repository.customImpl.VendaRepositoryImpl;
import com.example.SalesHub.repository.jpa.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaMapper mapper;
    private final VendaRepository repository;
    private final VendaRepositoryImpl customRepository;
    private final UsuarioService usuarioService;
    private final ProdutoService produtoService;
    private final EstoqueService estoqueService;

    public VendaResponse salvar(VendaRequest request) {

        var usuario = usuarioService.buscarUsuarioExistente(
                request.usuarioId()
        );

        var produto = produtoService.buscarProdutoPeloId(
                request.produtoId()
        );

        var estoqueResponse = estoqueService.pegarQuantidadeDoProdutoDoEstoque(
                request.estoqueId(),
                request.quantidade()
        );

        var venda = mapper.toEntity(
                request,
                usuario,
                produto
        );

        venda.aplicarDesconto(
                request.preco(),
                request.desconto()
        );

        return mapper.toReponse(
                repository.save(venda),
                usuarioService.mapearUsuario(usuario),
                produtoService.mapearProduto(produto),
                estoqueResponse
        );
    }

    public Page<VendaProjection> buscar(VendaFilter filter, Pageable pageable) {
        return customRepository.buscarVendas(
                filter,
                pageable
        );
    }
}
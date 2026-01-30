package com.example.SalesHub.service;

import com.example.SalesHub.dto.filter.VendaFilter;
import com.example.SalesHub.dto.projection.VendaProjection;
import com.example.SalesHub.dto.request.VendaRequest;
import com.example.SalesHub.dto.response.entity.VendaResponse;
import com.example.SalesHub.mapper.VendaMapper;
import com.example.SalesHub.model.Estoque;
import com.example.SalesHub.model.Produto;
import com.example.SalesHub.model.Usuario;
import com.example.SalesHub.model.Venda;
import com.example.SalesHub.repository.customImpl.VendaRepositoryImpl;
import com.example.SalesHub.repository.jpa.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaMapper mapper;
    private final VendaRepository repository;
    private final VendaRepositoryImpl customRepository;
    private final UsuarioService usuarioService;
    private final ProdutoService produtoService;
    private final EstoqueService estoqueService;
    private final HistoricoService historicoService;

    public VendaResponse salvar(VendaRequest request) {

        var usuario = usuarioService.buscarUsuarioExistente(
                request.usuarioId()
        );

        var produto = produtoService.buscarProdutoPeloId(
                request.produtoId()
        );

        var estoque = estoqueService.buscarPorId(
                request.estoqueId()
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
                request.valor(),
                request.desconto()
        );

        System.out.println(venda.getValorTotalVendas());

        setarTotalDeVendas(
                venda
        );

        System.out.println(venda.getValorTotalVendas());

        salvarHistorico(
                usuario,
                produto,
                estoque
        );

        return mapper.toReponse(
                repository.save(venda),
                usuarioService.mapearUsuario(usuario),
                produtoService.mapearProduto(produto),
                estoqueResponse
        );
    }

    private void salvarHistorico(Usuario usuario, Produto produto, Estoque estoque) {
        historicoService.salvar(
                usuario,
                produto,
                estoque
        );
    }

    private void setarTotalDeVendas(Venda venda) {

        var totalCalculado = totalVendas(venda);

        if (totalCalculado != null)
            venda.setValorTotalVendas(
                    totalCalculado.add(venda.getValor())
            );
    }

    private BigDecimal totalVendas(Venda venda) {
        return customRepository.buscarValorTotalDeVendas(venda);
    }

    public Page<VendaProjection> buscar(VendaFilter filter, Pageable pageable) {
        return customRepository.buscarVendas(
                filter,
                pageable
        );
    }
}
package com.example.SalesHub.service;

import com.example.SalesHub.dto.filter.HistoricoFilter;
import com.example.SalesHub.dto.projection.HistoricoProjection;
import com.example.SalesHub.dto.projection.MetricaProjection;
import com.example.SalesHub.mapper.HistoricoMapper;
import com.example.SalesHub.model.Estoque;
import com.example.SalesHub.model.Historico;
import com.example.SalesHub.model.Produto;
import com.example.SalesHub.model.Usuario;
import com.example.SalesHub.repository.customImpl.HistoricoRepositoryImpl;
import com.example.SalesHub.repository.jpa.HistoricoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HistoricoServiceTest {

    @Mock
    private HistoricoMapper mapper;

    @Mock
    private HistoricoRepository repository;

    @Mock
    private HistoricoRepositoryImpl customRepository;

    @InjectMocks
    private HistoricoService service;

    private Usuario usuario;
    private Produto produto;
    private Estoque estoque;
    private Historico historico;
    private BigDecimal quantidade;

    @BeforeEach
    void setup() {
        quantidade = new BigDecimal("10.00");

        usuario = Usuario.builder()
                .id(1L)
                .nome("Admin")
                .email("admin@email.com")
                .build();

        produto = Produto.builder()
                .id(2L)
                .nome("Teclado")
                .descricao("Teclado Mecânico")
                .ativo(true)
                .build();

        estoque = Estoque.builder()
                .id(3L)
                .quantidadeAtual(new BigDecimal("50.00"))
                .ativo(true)
                .build();

        historico = Historico.builder()
                .id(1L)
                .usuario(usuario)
                .produto(produto)
                .estoque(estoque)
                .quantidadeRetirada(quantidade)
                .criadoEm(LocalDateTime.now())
                .build();
    }

    @Test
    void deve_salvar_historico_com_sucesso() {
        when(mapper.toEntity(usuario, produto, estoque, quantidade)).thenReturn(historico);

        service.salvar(usuario, produto, estoque, quantidade);

        verify(mapper).toEntity(usuario, produto, estoque, quantidade);
        verify(repository).save(historico);
    }

    @Test
    void deve_buscar_historico_paginado_com_sucesso() {
        var filter = HistoricoFilter.builder()
                .id(1L)
                .usuarioId(1L)
                .produtoId(2L)
                .estoqueId(3L)
                .quantidadeRetirada(new BigDecimal("10.00"))
                .build();

        var pageable = mock(Pageable.class);
        var pagedResponse = new PageImpl<HistoricoProjection>(List.of());

        when(customRepository.buscarHistorico(filter, pageable)).thenReturn(pagedResponse);

        var resultado = service.buscar(filter, pageable);

        assertThat(resultado).isNotNull();
        verify(customRepository).buscarHistorico(filter, pageable);
    }

    @Test
    void deve_buscar_metricas_com_sucesso() {
        var metricaMock = mock(MetricaProjection.class);
        when(repository.buscarMetricas()).thenReturn(List.of(metricaMock));

        var resultado = service.buscarMetricas();

        assertThat(resultado).isNotEmpty();
        assertThat(resultado).hasSize(1);
        verify(repository).buscarMetricas();
    }
}
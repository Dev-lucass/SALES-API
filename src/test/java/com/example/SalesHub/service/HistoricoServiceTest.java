package com.example.SalesHub.service;

import com.example.SalesHub.dto.filter.HistoricoFilter;
import com.example.SalesHub.dto.projection.HistoricoProjection;
import com.example.SalesHub.mapper.HistoricoMapper;
import com.example.SalesHub.model.Estoque;
import com.example.SalesHub.model.Historico;
import com.example.SalesHub.model.Produto;
import com.example.SalesHub.model.Usuario;
import com.example.SalesHub.repository.customImpl.HistoricoRepositoryImpl;
import com.example.SalesHub.repository.jpa.HistoricoRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void deve_mapear_e_salvar_historico_corretamente() {
        var usuario = mock(Usuario.class);
        var produto = mock(Produto.class);
        var estoque = mock(Estoque.class);
        var historicoMapeado = mock(Historico.class);

        when(mapper.toEntity(usuario, produto, estoque)).thenReturn(historicoMapeado);

        service.salvar(usuario, produto, estoque);

        verify(mapper, times(1)).toEntity(usuario, produto, estoque);
        verify(repository, times(1)).save(historicoMapeado);
    }

    @Test
    void deve_buscar_historico_atraves_do_repositorio_customizado() {
        var filter = HistoricoFilter.builder().build();
        var pageable = PageRequest.of(0, 10);
        var content = List.of(mock(HistoricoProjection.class));
        var pageExpected = new PageImpl<>(content);

        when(customRepository.buscarHistorico(filter, pageable)).thenReturn(pageExpected);

        var resultado = service.buscar(filter, pageable);

        assertEquals(pageExpected, resultado);
        verify(customRepository, times(1)).buscarHistorico(filter, pageable);
    }

    @Test
    void deve_garantir_que_o_metodo_privado_de_mapeamento_use_o_mapper_injetado() {
        var usuario = new Usuario();
        var produto = new Produto();
        var estoque = new Estoque();
        var historicoEsperado = new Historico();

        when(mapper.toEntity(any(), any(), any())).thenReturn(historicoEsperado);

        service.salvar(usuario, produto, estoque);

        verify(mapper).toEntity(usuario, produto, estoque);
        verify(repository).save(historicoEsperado);
    }
}
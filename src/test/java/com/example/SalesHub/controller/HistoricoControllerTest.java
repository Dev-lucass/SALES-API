package com.example.SalesHub.controller;

import com.example.SalesHub.dto.filter.HistoricoFilter;
import com.example.SalesHub.dto.projection.HistoricoProjection;
import com.example.SalesHub.service.HistoricoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HistoricoController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HistoricoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HistoricoService service;

    private HistoricoProjection projection;

    @BeforeEach
    void setup() {
        projection = HistoricoProjection.builder()
                .id(1L)
                .usuarioId(10L)
                .produtoId(20L)
                .estoqueId(30L)
                .quantidadeRetirada(new BigDecimal("5.00"))
                .criadoEm(LocalDateTime.now())
                .build();
    }

    @Test
    void deve_buscar_historico_paginado_com_sucesso() throws Exception {
        var pagina = new PageImpl<>(List.of(projection));

        when(service.buscar(any(HistoricoFilter.class), any(Pageable.class))).thenReturn(pagina);

        mockMvc.perform(get("/api/v1/historico")
                        .param("usuarioId", "10")
                        .param("produtoId", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].usuarioId").value(10L))
                .andExpect(jsonPath("$.content[0].quantidadeRetirada").value(5.00))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(service).buscar(any(HistoricoFilter.class), any(Pageable.class));
    }

    @Test
    void deve_buscar_historico_sem_filtros_e_retornar_vazio() throws Exception {
        var paginaVazia = new PageImpl<HistoricoProjection>(List.of());

        when(service.buscar(any(HistoricoFilter.class), any(Pageable.class))).thenReturn(paginaVazia);

        mockMvc.perform(get("/api/v1/historico")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0));

        verify(service).buscar(any(HistoricoFilter.class), any(Pageable.class));
    }
}
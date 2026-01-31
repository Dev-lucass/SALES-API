package com.example.SalesHub.controller;

import com.example.SalesHub.dto.filter.HistoricoFilter;
import com.example.SalesHub.dto.projection.HistoricoProjection;
import com.example.SalesHub.service.HistoricoService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Test
    void deve_retornar_paginacao_de_historico_com_sucesso() throws Exception {
        var criadoEm = LocalDateTime.parse("31/01/2026 10:00:00", formatter);
        var projection = HistoricoProjection.builder()
                .id(1L)
                .usuarioId(10L)
                .criadoEm(criadoEm)
                .build();

        var page = new PageImpl<>(List.of(projection));

        when(service.buscar(any(HistoricoFilter.class), any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/historico")
                        .param("usuarioId", "10")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].usuarioId").value(10L))
                .andExpect(jsonPath("$.content[0].criadoEm").value("31/01/2026 10:00:00"));
    }

    @Test
    void deve_aceitar_filtro_de_datas_via_query_params() throws Exception {
        var page = new PageImpl<HistoricoProjection>(List.of());

        when(service.buscar(any(HistoricoFilter.class), any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/historico")
                        .param("dataInicial", "2026-01-01")
                        .param("dataFinal", "2026-01-31")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deve_retornar_vazio_quando_nao_houver_resultados() throws Exception {
        var pageVazia = new PageImpl<HistoricoProjection>(List.of());

        when(service.buscar(any(HistoricoFilter.class), any())).thenReturn(pageVazia);

        mockMvc.perform(get("/api/v1/historico")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0));
    }
}
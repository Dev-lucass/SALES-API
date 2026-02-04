package com.example.SalesHub.controller;

import com.example.SalesHub.dto.filter.EstoqueFilter;
import com.example.SalesHub.dto.projection.EstoqueProjection;
import com.example.SalesHub.dto.request.EstoqueRequest;
import com.example.SalesHub.dto.response.entity.EstoqueResponse;
import com.example.SalesHub.service.EstoqueService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EstoqueController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EstoqueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EstoqueService service;

    private EstoqueRequest request;
    private EstoqueResponse response;
    private EstoqueProjection projection;

    @BeforeEach
    void setup() {
        request = EstoqueRequest.builder()
                .produtoId(1L)
                .quantidade(new BigDecimal("100.00"))
                .build();

        response = EstoqueResponse.builder()
                .id(1L)
                .produtoId(1L)
                .quantidadeInicial(new BigDecimal("100.00"))
                .quantidadeAtual(new BigDecimal("100.00"))
                .build();

        projection = EstoqueProjection.builder()
                .id(1L)
                .produtoId(1L)
                .quantidadeInicial(new BigDecimal("100.00"))
                .quantidadeAtual(new BigDecimal("100.00"))
                .build();
    }

    @Test
    void deve_salvar_estoque_com_sucesso() throws Exception {
        when(service.salvar(any(EstoqueRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/estoque")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.produtoId").value(1L))
                .andExpect(jsonPath("$.quantidadeAtual").value(100.00));

        verify(service).salvar(any(EstoqueRequest.class));
    }

    @Test
    void deve_buscar_estoque_paginado() throws Exception {
        var pagina = new PageImpl<>(List.of(projection));
        when(service.buscar(any(EstoqueFilter.class), any(Pageable.class))).thenReturn(pagina);

        mockMvc.perform(get("/api/v1/estoque")
                        .param("produtoId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(service).buscar(any(EstoqueFilter.class), any(Pageable.class));
    }

    @Test
    void deve_atualizar_estoque_com_sucesso() throws Exception {
        mockMvc.perform(put("/api/v1/estoque/{estoqueId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(service).atualizar(eq(1L), any(EstoqueRequest.class));
    }

    @Test
    void deve_desativar_estoque_com_sucesso() throws Exception {
        mockMvc.perform(delete("/api/v1/estoque/{estoqueId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(service).desativar(1L);
    }

    @Test
    void deve_retornar_bad_request_ao_salvar_com_dados_invalidos() throws Exception {
        var requestInvalido = EstoqueRequest.builder()
                .produtoId(null)
                .quantidade(new BigDecimal("-10"))
                .build();

        mockMvc.perform(post("/api/v1/estoque")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());
    }
}
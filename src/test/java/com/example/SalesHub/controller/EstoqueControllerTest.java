package com.example.SalesHub.controller;

import com.example.SalesHub.dto.projection.EstoqueProjection;
import com.example.SalesHub.dto.request.EstoqueRequest;
import com.example.SalesHub.dto.response.entity.EstoqueResponse;
import com.example.SalesHub.service.EstoqueService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
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

    @Test
    void deve_salvar_estoque_com_sucesso() throws Exception {
        var request = EstoqueRequest.builder()
                .produtoId(1L)
                .quantidade(10L)
                .build();

        var response = EstoqueResponse.builder()
                .id(1L)
                .produtoId(1L)
                .quantidadeAtual(10L)
                .build();

        when(service.salvar(any(EstoqueRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/estoque")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.quantidadeAtual").value(10L));
    }

    @Test
    void deve_buscar_estoques_paginados() throws Exception {
        var projection = EstoqueProjection.builder()
                .id(1L)
                .produtoId(1L)
                .quantidadeAtual(50L)
                .build();

        var pagina = new PageImpl<>(List.of(projection), PageRequest.of(0, 10), 1);

        when(service.buscar(any(), any())).thenReturn(pagina);

        mockMvc.perform(get("/api/v1/estoque")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void deve_atualizar_estoque_com_sucesso() throws Exception {
        var estoqueId = 1L;
        var request = EstoqueRequest.builder()
                .produtoId(1L)
                .quantidade(20L)
                .build();

        doNothing().when(service).atualizar(eq(estoqueId), any(EstoqueRequest.class));

        mockMvc.perform(put("/api/v1/estoque/{estoqueId}", estoqueId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deve_desativar_estoque_com_sucesso() throws Exception {
        var estoqueId = 1L;

        doNothing().when(service).desativar(estoqueId);

        mockMvc.perform(delete("/api/v1/estoque/{estoqueId}", estoqueId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deve_retornar_bad_request_ao_salvar_com_dados_invalidos() throws Exception {
        var requestInvalido = EstoqueRequest.builder()
                .produtoId(null)
                .quantidade(0L)
                .build();

        mockMvc.perform(post("/api/v1/estoque")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());
    }
}
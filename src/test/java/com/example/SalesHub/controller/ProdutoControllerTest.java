package com.example.SalesHub.controller;

import com.example.SalesHub.dto.request.ProdutoRequest;
import com.example.SalesHub.dto.response.entity.ProdutoResponse;
import com.example.SalesHub.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProdutoController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProdutoService service;

    @Test
    void deve_salvar_produto_com_sucesso() throws Exception {
        var request = ProdutoRequest.builder()
                .nome("Teclado")
                .descricao("Teclado Mecânico")
                .build();

        var response = ProdutoResponse.builder()
                .id(1L)
                .nome("Teclado")
                .build();

        when(service.salvar(any(ProdutoRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/produto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Teclado"));
    }

    @Test
    void deve_retornar_bad_request_ao_salvar_produto_invalido() throws Exception {
        var requestInvalido = ProdutoRequest.builder()
                .nome("")
                .descricao("")
                .build();

        mockMvc.perform(post("/api/v1/produto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void deve_buscar_produtos_com_sucesso() throws Exception {
        mockMvc.perform(get("/api/v1/produto")
                        .param("nome", "Teclado")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(service).buscar(any(), any());
    }

    @Test
    void deve_atualizar_produto_com_sucesso() throws Exception {
        var id = 1L;
        var request = ProdutoRequest.builder()
                .nome("Novo Nome")
                .descricao("Nova Descrição")
                .build();

        mockMvc.perform(put("/api/v1/produto/{produtoId}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(service).atualizar(eq(id), any(ProdutoRequest.class));
    }

    @Test
    void deve_retornar_bad_request_ao_atualizar_com_dados_invalidos() throws Exception {
        var id = 1L;
        var requestInvalido = ProdutoRequest.builder().nome(" ").build();

        mockMvc.perform(put("/api/v1/produto/{produtoId}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deve_desativar_produto_com_sucesso() throws Exception {
        var id = 1L;

        mockMvc.perform(delete("/api/v1/produto/{produtoId}", id))
                .andExpect(status().isNoContent());

        verify(service).desativar(id);
    }
}
package com.example.SalesHub.controller;

import com.example.SalesHub.dto.request.ProdutoRequest;
import com.example.SalesHub.dto.response.entity.ProdutoResponse;
import com.example.SalesHub.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.math.BigDecimal;

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
                .preco(BigDecimal.valueOf(250.00))
                .build();

        var response = ProdutoResponse.builder()
                .id(1L)
                .nome("Teclado")
                .build();

        Mockito.when(service.salvar(Mockito.any(ProdutoRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/produto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Teclado"));
    }

    @Test
    void deve_retornar_bad_request_ao_salvar_produto_invalido() throws Exception {
        var requestInvalido = ProdutoRequest.builder()
                .nome("")
                .descricao("")
                .preco(BigDecimal.valueOf(-10))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/produto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verifyNoInteractions(service);
    }

    @Test
    void deve_buscar_produtos_com_sucesso() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/produto")
                        .param("nome", "Teclado")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(service).buscar(Mockito.any(), Mockito.any());
    }

    @Test
    void deve_atualizar_produto_com_sucesso() throws Exception {
        var id = 1L;
        var request = ProdutoRequest.builder()
                .nome("Novo Nome")
                .descricao("Nova Descrição")
                .preco(BigDecimal.valueOf(300.00))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/produto/{produtoId}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(service).atualizar(Mockito.eq(id), Mockito.any(ProdutoRequest.class));
    }

    @Test
    void deve_retornar_bad_request_ao_atualizar_com_dados_invalidos() throws Exception {
        var id = 1L;
        var requestInvalido = ProdutoRequest.builder().nome(" ").build();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/produto/{produtoId}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deve_desativar_produto_com_sucesso() throws Exception {
        var id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/produto/{produtoId}", id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(service).desativar(id);
    }
}
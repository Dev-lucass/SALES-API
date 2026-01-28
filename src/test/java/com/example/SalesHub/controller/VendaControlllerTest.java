package com.example.SalesHub.controller;

import com.example.SalesHub.dto.filter.VendaFilter;
import com.example.SalesHub.dto.request.VendaRequest;
import com.example.SalesHub.dto.response.entity.VendaResponse;
import com.example.SalesHub.service.VendaService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VendaControlller.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VendaControlllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VendaService service;

    private VendaRequest requestValida;
    private VendaResponse response;

    @BeforeEach
    void setup() {
        requestValida = VendaRequest.builder()
                .usuarioId(1L)
                .produtoId(1L)
                .estoqueId(1L)
                .quantidade(5L)
                .preco(new BigDecimal("100.0"))
                .desconto(10.0)
                .build();

        response = VendaResponse.builder()
                .id(1L)
                .quantidade(5L)
                .build();
    }

    @Test
    void deve_retornar_201_ao_salvar_venda_valida() throws Exception {
        var json = objectMapper.writeValueAsString(requestValida);

        when(service.salvar(any(VendaRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/venda")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.quantidade").value(5L));
    }

    @Test
    void deve_retornar_400_ao_salvar_venda_com_dados_invalidos() throws Exception {
        var requestInvalida = VendaRequest.builder().build();
        var json = objectMapper.writeValueAsString(requestInvalida);

        mockMvc.perform(post("/api/v1/venda")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deve_retornar_200_ao_buscar_vendas_com_filtros() throws Exception {

        when(service.buscar(any(VendaFilter.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/api/v1/venda")
                        .param("usuarioId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void deve_retornar_400_quando_quantidade_for_menor_que_o_minimo() throws Exception {
        var requestQuantidadeInvalida = VendaRequest.builder()
                .usuarioId(1L)
                .produtoId(1L)
                .estoqueId(1L)
                .quantidade(0L)
                .preco(BigDecimal.TEN)
                .desconto(0.0)
                .build();

        var json = objectMapper.writeValueAsString(requestQuantidadeInvalida);

        mockMvc.perform(post("/api/v1/venda")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}
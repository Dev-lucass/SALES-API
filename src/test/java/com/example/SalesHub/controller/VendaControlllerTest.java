package com.example.SalesHub.controller;

import com.example.SalesHub.dto.filter.VendaFilter;
import com.example.SalesHub.dto.projection.VendaProjection;
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
import static org.mockito.Mockito.verify;
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

    private VendaRequest request;
    private VendaResponse response;
    private VendaProjection projection;

    @BeforeEach
    void setup() {
        request = VendaRequest.builder()
                .usuarioId(1L)
                .produtoId(2L)
                .estoqueId(3L)
                .quantidade(new BigDecimal("2.00"))
                .valor(new BigDecimal("100.00"))
                .desconto(10.0)
                .build();

        response = VendaResponse.builder()
                .id(1L)
                .valor(new BigDecimal("90.00"))
                .quantidade(new BigDecimal("2.00"))
                .valorTotalVendas(new BigDecimal("90.00"))
                .build();

        projection = VendaProjection.builder()
                .id(1L)
                .usuarioId(1L)
                .usuario("Vendedor")
                .valor(new BigDecimal("90.00"))
                .quantidade(new BigDecimal("2.00"))
                .valorTotalVendas(new BigDecimal("90.00"))
                .build();
    }

    @Test
    void deve_salvar_venda_com_sucesso() throws Exception {
        when(service.salvar(any(VendaRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/venda")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.valor").value(90.00))
                .andExpect(jsonPath("$.quantidade").value(2.00));

        verify(service).salvar(any(VendaRequest.class));
    }

    @Test
    void deve_buscar_vendas_paginado_com_sucesso() throws Exception {
        var pagina = new PageImpl<>(List.of(projection));
        when(service.buscar(any(VendaFilter.class), any(Pageable.class))).thenReturn(pagina);

        mockMvc.perform(get("/api/v1/venda")
                        .param("usuarioId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].usuario").value("Vendedor"))
                .andExpect(jsonPath("$.content[0].quantidade").value(2.00))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(service).buscar(any(VendaFilter.class), any(Pageable.class));
    }

    @Test
    void deve_retornar_bad_request_ao_salvar_venda_invalida() throws Exception {
        var requestInvalido = VendaRequest.builder()
                .usuarioId(null)
                .quantidade(new BigDecimal("-1"))
                .build();

        mockMvc.perform(post("/api/v1/venda")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());
    }
}
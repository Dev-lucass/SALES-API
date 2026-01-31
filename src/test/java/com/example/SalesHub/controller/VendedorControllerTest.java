package com.example.SalesHub.controller;

import com.example.SalesHub.dto.filter.VendedorFilter;
import com.example.SalesHub.dto.projection.VendedorProjection;
import com.example.SalesHub.dto.request.VendedorReativacaoRequest;
import com.example.SalesHub.dto.request.VendedorRequest;
import com.example.SalesHub.dto.response.entity.VendedorReponse;
import com.example.SalesHub.service.VendedorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VendedorController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VendedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VendedorService service;

    @Test
    void deve_salvar_vendedor_com_sucesso() throws Exception {
        var request = VendedorRequest.builder()
                .usuarioId(1L)
                .cpf("11144477735")
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .build();

        var response = VendedorReponse.builder().id(1L).build();

        when(service.salvar(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/vendedor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deve_buscar_vendedores_com_filtros_na_url() throws Exception {
        var page = new PageImpl<VendedorProjection>(List.of());

        when(service.buscar(any(VendedorFilter.class), any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/vendedor")
                        .param("usuarioId", "1")
                        .param("dataInicial", "2026-01-01")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(service).buscar(any(), any());
    }

    @Test
    void deve_atualizar_vendedor_com_sucesso() throws Exception {
        var request = VendedorRequest.builder()
                .usuarioId(1L)
                .cpf("11144477735")
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .build();

        mockMvc.perform(put("/api/v1/vendedor/{vendedorId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(service).atualizar(eq(1L), any());
    }

    @Test
    void deve_desativar_vendedor_com_sucesso() throws Exception {
        mockMvc.perform(delete("/api/v1/vendedor/{vendedorId}", 1L))
                .andExpect(status().isNoContent());

        verify(service).desativar(1L);
    }

    @Test
    void deve_reativar_conta_vendedor_com_sucesso() throws Exception {
        var request = VendedorReativacaoRequest.builder()
                .vendedorId(1L)
                .cpf("11144477735")
                .build();

        mockMvc.perform(patch("/api/v1/vendedor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(service).reativar(any());
    }

    @Test
    void deve_retornar_bad_request_quando_cpf_for_invalido() throws Exception {
        var request = VendedorRequest.builder()
                .usuarioId(1L)
                .cpf("11122233344")
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .build();

        mockMvc.perform(post("/api/v1/vendedor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, never()).salvar(any());
    }
}
package com.example.SalesHub.controller;

import com.example.SalesHub.dto.request.UsuarioRequest;
import com.example.SalesHub.dto.response.entity.UsuarioResponse;
import com.example.SalesHub.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioService service;

    @Test
    void deve_retornar_201_ao_salvar_usuario_valido() throws Exception {
        var request = UsuarioRequest.builder()
                .nome("Teste")
                .email("teste@email.com")
                .senha("senha123")
                .build();

        var response = UsuarioResponse.builder()
                .id(1L)
                .nome("Teste")
                .email("teste@email.com")
                .criadoEm(LocalDateTime.now())
                .build();

        when(service.salvar(any(UsuarioRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Teste"));
    }

    @Test
    void deve_retornar_200_ao_buscar_usuarios() throws Exception {
        when(service.buscar(any(), any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/usuario")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service).buscar(any(), any());
    }

    @Test
    void deve_retornar_204_ao_atualizar_usuario() throws Exception {
        var usuarioId = 1L;
        var request = UsuarioRequest.builder()
                .nome("Nome Atualizado")
                .email("atualizado@email.com")
                .senha("novaSenha123")
                .build();

        mockMvc.perform(put("/api/v1/usuario/{usuarioId}", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(service).atualizar(eq(usuarioId), any(UsuarioRequest.class));
    }

    @Test
    void deve_retornar_204_ao_desativar_usuario() throws Exception {
        var usuarioId = 1L;

        mockMvc.perform(delete("/api/v1/usuario/{usuarioId}", usuarioId))
                .andExpect(status().isNoContent());

        verify(service).desativar(usuarioId);
    }
}
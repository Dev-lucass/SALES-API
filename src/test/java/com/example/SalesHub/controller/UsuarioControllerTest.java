package com.example.SalesHub.controller;

import com.example.SalesHub.dto.request.UsuarioRequest;
import com.example.SalesHub.dto.response.entity.UsuarioResponse;
import com.example.SalesHub.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

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

        Mockito.when(service.salvar(Mockito.any(UsuarioRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Teste"));
    }

    @Test
    void deve_retornar_200_ao_buscar_usuarios() throws Exception {
        Mockito.when(service.buscar(Mockito.any(), Mockito.any())).thenReturn(Page.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/usuario")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deve_retornar_204_ao_atualizar_usuario() throws Exception {
        var usuarioId = 1L;
        var request = UsuarioRequest.builder()
                .nome("Nome Atualizado")
                .email("atualizado@email.com")
                .senha("novaSenha123")
                .build();

        var response = UsuarioResponse.builder()
                .id(usuarioId)
                .nome("Nome Atualizado")
                .build();

        Mockito.when(service.atualizar(Mockito.eq(usuarioId), Mockito.any(UsuarioRequest.class)))
                .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/usuario/{usuarioId}", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(service).atualizar(Mockito.eq(usuarioId), Mockito.any(UsuarioRequest.class));
    }

    @Test
    void deve_retornar_204_ao_desativar_usuario() throws Exception {
        var usuarioId = 1L;

        Mockito.doNothing().when(service).desativar(usuarioId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/usuario/{usuarioId}", usuarioId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(service).desativar(usuarioId);
    }
}
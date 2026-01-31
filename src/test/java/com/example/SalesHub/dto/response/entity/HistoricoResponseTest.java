package com.example.SalesHub.dto.response.entity;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HistoricoResponseTest {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Test
    void deve_criar_instancia_completa_com_objetos_aninhados() {
        var agora = LocalDateTime.parse("31/01/2026 11:25:00", formatter);
        var usuario = Mockito.mock(UsuarioResponse.class);
        var produto = Mockito.mock(ProdutoResponse.class);
        var estoque = Mockito.mock(EstoqueResponse.class);

        var response = HistoricoResponse.builder()
                .id(1L)
                .usuario(usuario)
                .produto(produto)
                .estoque(estoque)
                .criadoEm(agora)
                .build();

        assertAll(
                () -> assertEquals(1L, response.id()),
                () -> assertEquals(usuario, response.usuario()),
                () -> assertEquals(produto, response.produto()),
                () -> assertEquals(estoque, response.estoque()),
                () -> assertEquals(agora, response.criadoEm()),
                () -> assertEquals("31/01/2026 11:25:00", response.criadoEm().format(formatter))
        );
    }

    @Test
    void deve_validar_integridade_da_data_e_hora_no_limite_do_segundo() {
        var dataOriginal = LocalDateTime.now();
        var response = HistoricoResponse.builder().criadoEm(dataOriginal).build();

        var textoFormatado = response.criadoEm().format(formatter);
        var dataRecuperada = LocalDateTime.parse(textoFormatado, formatter);

        assertEquals(dataOriginal.getMinute(), dataRecuperada.getMinute());
        assertEquals(dataOriginal.getSecond(), dataRecuperada.getSecond());
    }

    @Test
    void deve_permitir_campos_nulos_sem_lancar_excecao() {
        var response = HistoricoResponse.builder().id(99L).build();

        assertAll(
                () -> assertEquals(99L, response.id()),
                () -> assertNull(response.usuario()),
                () -> assertNull(response.produto()),
                () -> assertNull(response.estoque()),
                () -> assertNull(response.criadoEm())
        );
    }

    @Test
    void deve_manter_igualdade_entre_records_com_mesmo_estado() {
        var usuario = Mockito.mock(UsuarioResponse.class);
        var data = LocalDateTime.of(2026, 1, 31, 10, 0);

        var r1 = HistoricoResponse.builder().id(1L).usuario(usuario).criadoEm(data).build();
        var r2 = HistoricoResponse.builder().id(1L).usuario(usuario).criadoEm(data).build();

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void deve_confirmar_que_objeto_e_um_record() {
        var response = HistoricoResponse.builder().build();
        assertTrue(response.getClass().isRecord());
    }
}
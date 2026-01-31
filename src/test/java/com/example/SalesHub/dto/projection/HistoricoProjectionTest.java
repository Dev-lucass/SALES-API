package com.example.SalesHub.dto.projection;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HistoricoProjectionTest {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Test
    void deve_criar_instancia_completa_pelo_builder() {
        var dataHoraStr = "15/02/2026 14:30:45";
        var criadoEm = LocalDateTime.parse(dataHoraStr, formatter);

        var projection = HistoricoProjection.builder()
                .id(1L)
                .usuarioId(20L)
                .produtoId(300L)
                .estoqueId(4000L)
                .criadoEm(criadoEm)
                .build();

        assertAll(
                () -> assertEquals(1L, projection.id()),
                () -> assertEquals(20L, projection.usuarioId()),
                () -> assertEquals(300L, projection.produtoId()),
                () -> assertEquals(4000L, projection.estoqueId()),
                () -> assertEquals(criadoEm, projection.criadoEm()),
                () -> assertEquals("15/02/2026 14:30:45", projection.criadoEm().format(formatter))
        );
    }

    @Test
    void deve_validar_formatacao_da_data_e_hora() {
        var agora = LocalDateTime.now();
        var projection = HistoricoProjection.builder().criadoEm(agora).build();

        var formatado = projection.criadoEm().format(formatter);
        var recuperado = LocalDateTime.parse(formatado, formatter);

        assertEquals(agora.getSecond(), recuperado.getSecond());
        assertEquals(agora.getDayOfMonth(), recuperado.getDayOfMonth());
    }

    @Test
    void deve_permitir_valores_nulos_no_projection() {
        var projection = HistoricoProjection.builder().build();

        assertAll(
                () -> assertNull(projection.id()),
                () -> assertNull(projection.usuarioId()),
                () -> assertNull(projection.criadoEm())
        );
    }

    @Test
    void deve_garantir_igualdade_entre_records_com_mesmos_dados() {
        var data = LocalDateTime.parse("20/05/2026 10:00:00", formatter);

        var p1 = HistoricoProjection.builder().id(1L).criadoEm(data).build();
        var p2 = HistoricoProjection.builder().id(1L).criadoEm(data).build();

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void deve_ser_um_record_com_comportamento_imutavel() {
        var projection = HistoricoProjection.builder().id(100L).build();

        assertTrue(projection.getClass().isRecord());
        assertEquals(100L, projection.id());
    }
}
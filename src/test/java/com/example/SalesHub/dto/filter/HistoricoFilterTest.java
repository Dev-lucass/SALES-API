package com.example.SalesHub.dto.filter;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HistoricoFilterTest {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Test
    void deve_criar_instancia_com_todos_os_campos_pelo_builder() {
        var dataInicialStr = "01/01/2026";
        var dataFinalStr = "31/01/2026";
        var dataInicial = LocalDate.parse(dataInicialStr, formatter);
        var dataFinal = LocalDate.parse(dataFinalStr, formatter);

        var filter = HistoricoFilter.builder()
                .id(1L)
                .usuarioId(10L)
                .produtoId(100L)
                .estoqueId(1000L)
                .dataInicial(dataInicial)
                .dataFinal(dataFinal)
                .build();

        assertAll(
                () -> assertEquals(1L, filter.id()),
                () -> assertEquals(10L, filter.usuarioId()),
                () -> assertEquals(100L, filter.produtoId()),
                () -> assertEquals(1000L, filter.estoqueId()),
                () -> assertEquals(dataInicial, filter.dataInicial()),
                () -> assertEquals(dataFinal, filter.dataFinal()),
                () -> assertEquals("01/01/2026", filter.dataInicial().format(formatter)),
                () -> assertEquals("31/01/2026", filter.dataFinal().format(formatter))
        );
    }

    @Test
    void deve_permitir_campos_nulos() {
        var filter = HistoricoFilter.builder()
                .id(null)
                .dataInicial(null)
                .build();

        assertAll(
                () -> assertNull(filter.id()),
                () -> assertNull(filter.usuarioId()),
                () -> assertNull(filter.dataInicial())
        );
    }

    @Test
    void deve_garantir_igualdade_de_objetos_iguais() {
        var data = LocalDate.parse("15/05/2026", formatter);

        var filter1 = HistoricoFilter.builder().id(1L).dataInicial(data).build();
        var filter2 = HistoricoFilter.builder().id(1L).dataInicial(data).build();

        assertEquals(filter1, filter2);
        assertEquals(filter1.hashCode(), filter2.hashCode());
    }

    @Test
    void deve_validar_formatacao_especifica_da_data() {
        var dataEsperada = "25/12/2026";
        var data = LocalDate.parse(dataEsperada, formatter);

        var filter = HistoricoFilter.builder().dataFinal(data).build();

        var dataFormatada = filter.dataFinal().format(formatter);

        assertEquals(dataEsperada, dataFormatada);
        assertEquals(2026, filter.dataFinal().getYear());
        assertEquals(12, filter.dataFinal().getMonthValue());
        assertEquals(25, filter.dataFinal().getDayOfMonth());
    }

    @Test
    void deve_comportar_se_como_record_imutavel() {
        var filter = HistoricoFilter.builder().id(50L).build();

        assertNotNull(filter.id());
        assertTrue(filter.getClass().isRecord());
    }
}
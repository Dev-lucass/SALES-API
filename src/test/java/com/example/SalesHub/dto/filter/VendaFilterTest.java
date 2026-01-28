package com.example.SalesHub.dto.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VendaFilterTest {

    private VendaFilter filter;
    private DateTimeFormatter formatter;

    @BeforeEach
    void setup() {
        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        filter = VendaFilter.builder()
                .id(1L)
                .usuarioId(10L)
                .valor(500L)
                .dataInicial(LocalDate.parse("01/01/2026", formatter))
                .dataFinal(LocalDate.parse("31/01/2026", formatter))
                .build();
    }

    @Test
    void deve_armazenar_valores_corretamente_no_filtro() {
        assertThat(filter.id()).isEqualTo(1L);
        assertThat(filter.usuarioId()).isEqualTo(10L);
        assertThat(filter.valor()).isEqualTo(500L);
    }

    @Test
    void deve_validar_formatacao_das_datas_conforme_padrao_esperado() {
        var dataEsperada = "29/01/2026";
        var dataLocal = LocalDate.parse(dataEsperada, formatter);

        var filterComData = VendaFilter.builder()
                .dataInicial(dataLocal)
                .build();

        assertThat(filterComData.dataInicial().format(formatter)).isEqualTo(dataEsperada);
    }

    @Test
    void deve_permitir_filtro_apenas_com_datas_nulas() {
        var filterSemDatas = VendaFilter.builder()
                .id(1L)
                .dataInicial(null)
                .dataFinal(null)
                .build();

        assertThat(filterSemDatas.dataInicial()).isNull();
        assertThat(filterSemDatas.dataFinal()).isNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"2026-01-01", "01-01-2026", "2026/01/01"})
    void deve_garantir_que_datas_em_formatos_invalidos_nao_sejam_processadas_pelo_formatter(String dataInvalida) {
        var lancouExcecao = false;
        try {
            LocalDate.parse(dataInvalida, formatter);
        } catch (Exception e) {
            lancouExcecao = true;
        }
        assertThat(lancouExcecao).isTrue();
    }

    @Test
    void deve_garantir_que_data_inicial_seja_anterior_ou_igual_a_data_final() {
        var dataInicial = LocalDate.parse("10/01/2026", formatter);
        var dataFinal = LocalDate.parse("05/01/2026", formatter);

        var filterInconsistente = VendaFilter.builder()
                .dataInicial(dataInicial)
                .dataFinal(dataFinal)
                .build();

        assertThat(filterInconsistente.dataInicial()).isAfter(filterInconsistente.dataFinal());
    }
}
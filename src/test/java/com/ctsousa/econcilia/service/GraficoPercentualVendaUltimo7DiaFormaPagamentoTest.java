package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.ApplicationUnitTest;
import com.ctsousa.econcilia.model.dto.GraficoPercentualVendaUltimo7DiaDTO;
import com.ctsousa.econcilia.service.impl.GraficoPercentualVendaUltimo7DiaFormaPagamentoImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class GraficoPercentualVendaUltimo7DiaFormaPagamentoTest extends ApplicationUnitTest {

    @Test
    void deveGerarGraficoVenda() {
        GraficoVendaService<GraficoPercentualVendaUltimo7DiaDTO> grafico = new GraficoPercentualVendaUltimo7DiaFormaPagamentoImpl();

        GraficoPercentualVendaUltimo7DiaDTO graficoDTO = grafico.processar(getVendas());

        Assertions.assertFalse(graficoDTO.getData().isEmpty());
        Assertions.assertFalse(graficoDTO.getLabels().isEmpty());

        Assertions.assertTrue(graficoDTO.getData()
                .stream()
                .allMatch(valor -> valor.compareTo(BigDecimal.ZERO) > 0));
    }
}

package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.ApplicationUnitTest;
import com.ctsousa.econcilia.model.dto.GraficoVendaUltimo7DiaDinheiroPixDTO;
import com.ctsousa.econcilia.service.impl.GraficoVendaUltimo7DiaDinheiroPixServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class GraficoVendaUltimo7DiaDinheiroPixServiceTest extends ApplicationUnitTest {

    @Test
    void deveGerarGraficoVenda() {
        GraficoVendaService<GraficoVendaUltimo7DiaDinheiroPixDTO> grafico = new GraficoVendaUltimo7DiaDinheiroPixServiceImpl();

        GraficoVendaUltimo7DiaDinheiroPixDTO graficoDTO = grafico.processar(getVendas());

        Assertions.assertFalse(graficoDTO.getDataCash().isEmpty());
        Assertions.assertFalse(graficoDTO.getDataPix().isEmpty());
        Assertions.assertFalse(graficoDTO.getLabels().isEmpty());

        var valorPix = graficoDTO.getDataPix().stream()
                .filter(valor -> valor != null && valor.compareTo(BigDecimal.ZERO) > 0)
                .findFirst().orElse(BigDecimal.ZERO);

        var valorCash = graficoDTO.getDataCash().stream()
                .filter(valor -> valor != null && valor.compareTo(BigDecimal.ZERO) > 0)
                .findFirst().orElse(BigDecimal.ZERO);

        Assertions.assertEquals(new BigDecimal("740.0"), valorPix);
        Assertions.assertEquals(new BigDecimal("1100.0"), valorCash);
    }
}

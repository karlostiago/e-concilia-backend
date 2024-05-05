package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.ApplicationUnitTest;
import com.ctsousa.econcilia.model.dto.GraficoVendaUltimo7DiaCreditoDebitoDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class GraficoVendaUltimo7DiaCreditoDebitoServiceTest extends ApplicationUnitTest {

    @Test
    void deveGerarGraficoVenda() {
        GraficoVendaService<GraficoVendaUltimo7DiaCreditoDebitoDTO> grafico = new GraficoVendaUltimo7DiaCreditoDebitoServiceImpl();

        GraficoVendaUltimo7DiaCreditoDebitoDTO graficoDTO = grafico.processar(getVendas());

        Assertions.assertFalse(graficoDTO.getDataCredit().isEmpty());
        Assertions.assertFalse(graficoDTO.getDataDebit().isEmpty());
        Assertions.assertFalse(graficoDTO.getLabels().isEmpty());

        var valorDebit = graficoDTO.getDataDebit().stream()
                .filter(valor -> valor != null && valor.compareTo(BigDecimal.ZERO) > 0)
                .findFirst().orElse(BigDecimal.ZERO);

        var valorCredit = graficoDTO.getDataCredit().stream()
                .filter(valor -> valor != null && valor.compareTo(BigDecimal.ZERO) > 0)
                .findFirst().orElse(BigDecimal.ZERO);

        Assertions.assertEquals(new BigDecimal("1000.0"), valorDebit);
        Assertions.assertEquals(new BigDecimal("800.0"), valorCredit);
    }
}

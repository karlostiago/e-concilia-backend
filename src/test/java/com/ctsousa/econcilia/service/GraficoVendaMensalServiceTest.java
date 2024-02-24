package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.ApplicationUnitTest;
import com.ctsousa.econcilia.model.dto.GraficoVendaMensalDTO;
import com.ctsousa.econcilia.service.impl.GraficoVendaMensalImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class GraficoVendaMensalServiceTest extends ApplicationUnitTest {

    @Test
    void deveGerarGraficoVenda() {
        GraficoVendaService<GraficoVendaMensalDTO> grafico = new GraficoVendaMensalImpl();

        GraficoVendaMensalDTO graficoDTO = grafico.processar(getVendas());

        Assertions.assertFalse(graficoDTO.getDataSets().isEmpty());
        Assertions.assertFalse(graficoDTO.getLabels().isEmpty());

        Assertions.assertEquals(BigDecimal.valueOf(17.46D), graficoDTO.getDataSets().stream()
                .flatMap(dataSet -> dataSet.getData().stream())
                .filter(data -> data.compareTo(BigDecimal.ZERO) > 0)
                .findFirst().orElse(BigDecimal.ZERO));
    }
}

package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.report.dto.RelatorioConsolidadoDTO;

import java.time.LocalDate;
import java.util.List;

public interface GraficoVendaService<T> {

    T processar(final List<Venda> vendas);

    T processar(LocalDate periodo, List<RelatorioConsolidadoDTO> consolidados);
}

package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.dto.RelatorioConsolidadoDTO;
import com.ctsousa.econcilia.processor.Processador;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public interface ConsolidacaoService {

    void consolidar(final Processador processador);

    BigDecimal buscarValorBruto(Empresa empresa, Operadora operadora, LocalDate dataInicial, LocalDate dataFinal);

    boolean temMensalidade(Empresa empresa, Operadora operadora, LocalDate dataInicial, LocalDate dataFinal);

    byte [] gerarDadosCSV(LocalDate dataInicial, LocalDate dataFinal, Empresa empresa, Operadora operadora);

    List<RelatorioConsolidadoDTO> gerarDadosPDF(LocalDate dataInicial, LocalDate dataFinal, Empresa empresa, Operadora operadora);
}

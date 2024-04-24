package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.processor.Processador;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public interface ConsolidacaoService extends GeradorRelatorioCSVService {

    void consolidar(final Processador processador);

    BigDecimal buscarValorBruto(Empresa empresa, Operadora operadora, LocalDate dataInicial, LocalDate dataFinal);

    boolean temMensalidade(Empresa empresa, Operadora operadora, LocalDate dataInicial, LocalDate dataFinal);
}

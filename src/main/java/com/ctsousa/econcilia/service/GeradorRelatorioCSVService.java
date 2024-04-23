package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface GeradorRelatorioCSVService {

    byte [] gerarCSV(LocalDate dataInicial, LocalDate dataFinal, Empresa empresa, Operadora operadora);
}

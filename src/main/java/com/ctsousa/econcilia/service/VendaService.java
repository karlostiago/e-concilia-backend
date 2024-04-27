package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.RelatorioTaxaDTO;
import com.ctsousa.econcilia.model.dto.RelatorioVendaDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface VendaService {

    List<Venda> buscar(Empresa empresa, Operadora operadora, LocalDate dtInicial, LocalDate dtFinal, String metodoPagamento, String bandeira, String tipoRecebimento);

    byte[] gerarDadosCSV(LocalDate dataInicial, LocalDate dataFinal, Empresa empresa, Operadora operadora);

    List<RelatorioVendaDTO> gerarDadosPDF(LocalDate dataInicial, LocalDate dataFinal, Empresa empresa, Operadora operadora);
}

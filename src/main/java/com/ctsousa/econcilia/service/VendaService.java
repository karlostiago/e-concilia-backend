package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Venda;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface VendaService extends GeradorRelatorioCSVService {

    List<Venda> buscar(Empresa empresa, Operadora operadora, LocalDate dtInicial, LocalDate dtFinal, String metodoPagamento, String bandeira, String tipoRecebimento);
}

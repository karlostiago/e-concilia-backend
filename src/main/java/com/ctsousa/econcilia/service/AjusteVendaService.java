package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.AjusteVenda;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface AjusteVendaService {

    List<AjusteVenda> buscar(String codigoLoja, LocalDate dataInicial, LocalDate dataFinal);
}

package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.VendaProcessada;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VendaProcessadaService {

    VendaProcessada processar(final List<Venda> vendas);
}

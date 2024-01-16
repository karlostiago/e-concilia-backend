package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Venda;

import java.util.List;

public interface GraficoVendaService<T> {

    T processar(final List<Venda> vendas);
}

package com.ctsousa.econcilia.processador;

import com.ctsousa.econcilia.model.Venda;

import java.util.List;

public interface Executor<T> {

    T processar(List<Venda> vendas);
}

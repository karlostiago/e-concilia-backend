package com.ctsousa.econcilia.processador;

import com.ctsousa.econcilia.model.Integracao;

import java.time.LocalDate;

public interface Executor {

    void processar(Integracao integracao, LocalDate dtInicial, LocalDate dtFinal, boolean executar);
}

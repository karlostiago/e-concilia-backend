package com.ctsousa.econcilia.processor;

public interface Executor {

    void processar(ProcessadorFiltro processadorFiltro, boolean executarCalculo, boolean consolidar);
}

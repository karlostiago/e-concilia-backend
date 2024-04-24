package com.ctsousa.econcilia.processor.ubereats;

import com.ctsousa.econcilia.processor.Processador;
import com.ctsousa.econcilia.processor.ProcessadorFiltro;
import org.springframework.stereotype.Component;

@Component
public class ProcessadorUberEats extends Processador {

    @Override
    public void processar(ProcessadorFiltro processadorFiltro, boolean executarCalculo, boolean consolidar) {
        throw new UnsupportedOperationException("Operação não suportada por esse processador.");
    }
}

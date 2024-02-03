package com.ctsousa.econcilia.processador.ubereats;

import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.processador.Processador;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ProcessadorUberEats extends Processador {

    @Override
    public void processar(Integracao integracao, LocalDate dtInicial, LocalDate dtFinal, boolean executar) {
        throw new UnsupportedOperationException("Operação não suportada por esse processador.");
    }
}

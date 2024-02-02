package com.ctsousa.econcilia.processador;

import com.ctsousa.econcilia.enumaration.TipoProcessador;
import com.ctsousa.econcilia.processador.ifood.ProcessadorIfood;
import com.ctsousa.econcilia.processador.ubereats.ProcessadorUberEats;
import org.springframework.stereotype.Component;

@Component
public class ProcessadorFactory {

    private final ProcessadorIfood processadorIfood;

    private  final ProcessadorUberEats processadorUberEats;

    public ProcessadorFactory(ProcessadorIfood processadorIfood, ProcessadorUberEats processadorUberEats) {
        this.processadorIfood = processadorIfood;
        this.processadorUberEats = processadorUberEats;
    }

    public Processador criar(TipoProcessador tipoProcessador) {
        return switch (tipoProcessador) {
            case IFOOD -> processadorIfood;
            case UBER_EATS -> processadorUberEats;
        };
    }
}

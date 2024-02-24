package com.ctsousa.econcilia.processor;

import com.ctsousa.econcilia.enumaration.TipoProcessador;
import com.ctsousa.econcilia.processor.ifood.ProcessadorIfood;
import com.ctsousa.econcilia.processor.ifood.ProcessadorIfoodMock;
import com.ctsousa.econcilia.processor.ubereats.ProcessadorUberEats;
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
            case IFOOD_MOCK -> new ProcessadorIfoodMock();
            case UBER_EATS -> processadorUberEats;
        };
    }
}

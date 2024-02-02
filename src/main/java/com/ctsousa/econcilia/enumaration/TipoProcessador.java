package com.ctsousa.econcilia.enumaration;

import com.ctsousa.econcilia.config.SpringConfig;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.processador.Processador;
import com.ctsousa.econcilia.processador.ProcessadorFactory;

public enum TipoProcessador {

    IFOOD,

    UBER_EATS;

    private Processador processador;

    public static Processador porOperadora(Operadora operadora) {
        return switch (operadora.getDescricao().toUpperCase()) {
            case "IFOOD" -> getProcessadorFactory().criar(IFOOD);
            case "UBER EATS" -> getProcessadorFactory().criar(UBER_EATS);
            default ->
                throw new NotificacaoException("Processador " + operadora.getDescricao() + ", não suportado!");
        };
    }

    private static ProcessadorFactory getProcessadorFactory() {
        return (ProcessadorFactory)SpringConfig.getBean("processadorFactory");
    }
}

package com.ctsousa.econcilia.enumaration;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.processador.Processador;
import com.ctsousa.econcilia.processador.ProcessadorFactory;

public enum TipoProcessador {

    IFOOD,

    UBER_EATS;

    private Processador<?> processador;

    public static Processador<?> porOperadora(Operadora operadora, ProcessadorFactory processadorFactory) {
        return switch (operadora.getDescricao().toUpperCase()) {
            case "IFOOD" -> processadorFactory.criar(IFOOD);
            case "UBER EATS" -> processadorFactory.criar(UBER_EATS);
            default ->
                throw new NotificacaoException("Processador " + operadora.getDescricao() + ", não suportado!");
        };
    }
}

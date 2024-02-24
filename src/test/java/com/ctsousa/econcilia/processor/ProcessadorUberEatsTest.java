package com.ctsousa.econcilia.processor;

import com.ctsousa.econcilia.processor.ubereats.ProcessadorUberEats;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProcessadorUberEatsTest {

    private Processador processador;

    @BeforeEach
    void setup() {
        processador = new ProcessadorUberEats();
    }

    @Test
    void deveRetornarOperacaoNaoSuportada() {
        var thrown = Assertions.assertThrows(UnsupportedOperationException.class, () -> processador.processar(null, false));

        Assertions.assertEquals("Operação não suportada por esse processador.", thrown.getMessage());
    }
}

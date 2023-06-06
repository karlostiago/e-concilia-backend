package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.integration.receitaws.json.DadosCnpjJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CnpjServiceTest {

    @Autowired
    private CnpjService cnpjService;

    @Test
    public void deveBuscarDadosQuandoCnpjValido() {
        DadosCnpjJson json = cnpjService.buscarCNPJ("45234574000162");

        Assertions.assertNotNull(json);
    }

    @Test
    public void naoDeveBuscarDadosQuandoCnpInvalido() {
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            cnpjService.buscarCNPJ("45234574000163");
        });

        Assertions.assertEquals("Cnpj inválido.", thrown.getMessage());
    }
}

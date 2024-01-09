package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.integration.receitaws.ReceitaWS;
import com.ctsousa.econcilia.integration.receitaws.json.DadosCnpjJson;
import com.ctsousa.econcilia.service.impl.CnpjServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;


class CnpjServiceTest {

    private CnpjService cnpjService;

    @BeforeEach
    void setup() {
        cnpjService = new CnpjServiceImpl(new ReceitaWS(new RestTemplate()));
    }

    @Test
    void deveBuscarDadosQuandoCnpjValido() {
        DadosCnpjJson json = cnpjService.buscarCNPJ("45234574000162");

        Assertions.assertNotNull(json);
    }

    @Test
    void naoDeveBuscarDadosQuandoCnpInvalido() {
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            cnpjService.buscarCNPJ("45234574000163");
        });

        Assertions.assertEquals("Cnpj 45234574000163 inválido.", thrown.getMessage());
    }
}

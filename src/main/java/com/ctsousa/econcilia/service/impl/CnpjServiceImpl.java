package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.integration.receitaws.ReceitaWS;
import com.ctsousa.econcilia.integration.receitaws.json.DadosCnpjJson;
import com.ctsousa.econcilia.service.CnpjService;
import com.ctsousa.econcilia.validation.CnpjValidator;
import org.springframework.stereotype.Component;

@Component
public class CnpjServiceImpl implements CnpjService {

    private final ReceitaWS receitaWS;

    public CnpjServiceImpl(ReceitaWS receitaWS) {
        this.receitaWS = receitaWS;
    }

    public DadosCnpjJson buscarCNPJ (final String cnpj) {
        new CnpjValidator(cnpj).validar();
        return this.receitaWS.consultarDadosCNPJ(cnpj);
    }
}

package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.integration.receitaws.ReceitaWS;
import com.ctsousa.econcilia.integration.receitaws.json.DadosCnpjJson;
import com.ctsousa.econcilia.service.CnpjService;
import com.ctsousa.econcilia.validation.CnpjValidator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CnpjServiceImpl implements CnpjService {

    private final ReceitaWS receitaWS;

    private final Map<String, DadosCnpjJson> cache;

    public CnpjServiceImpl(ReceitaWS receitaWS) {
        this.receitaWS = receitaWS;
        this.cache = new HashMap<>();
    }

    public DadosCnpjJson buscarCNPJ (final String cnpj) {
        new CnpjValidator(cnpj).validar();

        DadosCnpjJson dadosCnpjJson = cache.get(cnpj);

        if (dadosCnpjJson == null) {
            dadosCnpjJson = this.receitaWS.consultarDadosCNPJ(cnpj);
            cache.put(cnpj, dadosCnpjJson);
        }

        return dadosCnpjJson;
    }
}

package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.integration.receitaws.dto.DadosCnpjJson;
import com.ctsousa.econcilia.integration.receitaws.service.ReceitaWS;
import com.ctsousa.econcilia.mapper.impl.CancelamentoMapper;
import com.ctsousa.econcilia.validation.CnpjValidator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CnpjServiceImpl implements CancelamentoMapper.CnpjService {

    private final ReceitaWS receitaWS;

    private final Map<String, DadosCnpjJson> cache;

    public CnpjServiceImpl(ReceitaWS receitaWS) {

        this.receitaWS = receitaWS;
        this.cache = new HashMap<>();
    }

    public DadosCnpjJson buscarCNPJ(final String cnpj) {
        new CnpjValidator(cnpj).validar();

        DadosCnpjJson dadosCnpjJson = cache.get(cnpj);

        if (dadosCnpjJson == null) {
            dadosCnpjJson = this.receitaWS.consultarDadosCNPJ(cnpj);
            cache.put(cnpj, dadosCnpjJson);
        }

        return dadosCnpjJson;
    }
}

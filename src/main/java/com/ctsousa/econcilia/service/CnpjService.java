package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.integration.receitaws.dto.DadosCnpjJson;
import org.springframework.stereotype.Service;

@Service
public interface CnpjService {

    DadosCnpjJson buscarCNPJ(final String cnpj);
}

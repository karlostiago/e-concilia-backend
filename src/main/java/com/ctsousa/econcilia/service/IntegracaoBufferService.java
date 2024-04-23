package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.IntegracaoBuffer;
import com.ctsousa.econcilia.model.Operadora;
import org.springframework.stereotype.Service;

@Service
public interface IntegracaoBufferService {

    void salvar(IntegracaoBuffer integracaoBuffer);
}

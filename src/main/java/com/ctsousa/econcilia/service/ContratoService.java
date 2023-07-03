package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Contrato;
import org.springframework.stereotype.Service;

@Service
public interface ContratoService {

    Contrato salvar(Contrato contrato);
}

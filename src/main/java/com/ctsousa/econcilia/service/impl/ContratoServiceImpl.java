package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Contrato;
import com.ctsousa.econcilia.repository.ContratoRepository;
import com.ctsousa.econcilia.service.ContratoService;
import org.springframework.stereotype.Component;

@Component
public class ContratoServiceImpl implements ContratoService {

    private final ContratoRepository contratoRepository;

    public ContratoServiceImpl(ContratoRepository contratoRepository) {
        this.contratoRepository = contratoRepository;
    }

    @Override
    public Contrato salvar(Contrato contrato) {
        if (contratoRepository.existsByEmpresaAndOperadora(contrato.getEmpresa(), contrato.getOperadora())) {
            throw new NotificacaoException("Já existe um contrato para a empresa e operadora selecionados.");
        }
        return contratoRepository.save(contrato);
    }
}

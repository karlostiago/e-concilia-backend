package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Contrato;
import com.ctsousa.econcilia.model.dto.ContratoDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ContratoService {

    Contrato salvar(Contrato contrato);

    List<Contrato> pesquisar(final Long empresaId, final Long operadoraId);

    void deletar (Long id);

    Contrato atualizar (Long id, ContratoDTO contratoDTO);

    Contrato pesquisarPorId (Long id);

    Contrato ativar (Long id);

    Contrato desativar (Long id);
}

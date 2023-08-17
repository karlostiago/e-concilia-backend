package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.assembler.impl.ContratoMapper;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Contrato;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Taxa;
import com.ctsousa.econcilia.model.dto.ContratoDTO;
import com.ctsousa.econcilia.repository.ContratoRepository;
import com.ctsousa.econcilia.service.ContratoService;
import com.ctsousa.econcilia.service.TaxaService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContratoServiceImpl implements ContratoService {

    private final ContratoRepository contratoRepository;

    private final TaxaService taxaService;

    private final ContratoMapper contratoMapper;

    public ContratoServiceImpl(ContratoRepository contratoRepository, TaxaService taxaService, ContratoMapper contratoMapper) {
        this.contratoRepository = contratoRepository;
        this.taxaService = taxaService;
        this.contratoMapper = contratoMapper;
    }

    @Override
    public Contrato salvar(final Contrato contrato) {

        if (contratoRepository.existsByEmpresaAndOperadora(contrato.getEmpresa(), contrato.getOperadora())) {
            throw new NotificacaoException("Já existe um contrato para a empresa e operadora selecionados.");
        }

        validaTaxas(contrato.getTaxas());

        return contratoRepository.save(contrato);
    }

    @Override
    public List<Contrato> pesquisar(final Long empresaId, final Long operadoraId) {

        List<Contrato> contratos;
        var operadora = new Operadora(operadoraId);
        var empresa = new Empresa(empresaId);

        if (empresaId != null && operadoraId != null) {
            contratos = contratoRepository.findByEmpresaAndOperadora(empresa, operadora);
        }
        else if (empresaId != null) {
            contratos = contratoRepository.findByEmpresa(empresa);
        }
        else if (operadoraId != null) {
            contratos = contratoRepository.findByOperadora(operadora);
        }
        else {
            contratos = contratoRepository.findAll();
        }

        return contratos;
    }

    @Override
    public Contrato pesquisarPorId(final Long id) {
        return contratoRepository.porID(id)
                .orElseThrow(() -> new NotificacaoException(String.format("Contrato com id %d não encontrado", id)));
    }

    @Override
    public void deletar(Long id) {
        var contrato = pesquisarPorId(id);
        contratoRepository.delete(contrato);
    }

    @Override
    public Contrato atualizar(Long id, ContratoDTO contratoDTO) {
        pesquisarPorId(id);

        contratoDTO.setNumero(id);
        Contrato contrato = contratoMapper.paraEntidade(contratoDTO);
        contratoRepository.save(contrato);

        return contrato;
    }

    private void validaTaxas(final List<Taxa> taxas) {
        taxaService.verificaDuplicidade(taxas);
        taxaService.validaEntraEmVigor(taxas);
        taxas.forEach(taxaService::validar);
    }
}

package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.mapper.impl.ContratoMapper;
import com.ctsousa.econcilia.model.*;
import com.ctsousa.econcilia.model.dto.ContratoDTO;
import com.ctsousa.econcilia.repository.ContratoRepository;
import com.ctsousa.econcilia.service.ContratoService;
import com.ctsousa.econcilia.service.IntegracaoBufferService;
import com.ctsousa.econcilia.service.TaxaService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.ctsousa.econcilia.util.StringUtil.somenteNumero;

@Component
public class ContratoServiceImpl implements ContratoService {

    private final ContratoRepository contratoRepository;

    private final TaxaService taxaService;

    private final ContratoMapper contratoMapper;

    private final IntegracaoBufferService integracaoBufferService;

    public ContratoServiceImpl(ContratoRepository contratoRepository, TaxaService taxaService, ContratoMapper contratoMapper, IntegracaoBufferService integracaoBufferService) {
        this.contratoRepository = contratoRepository;
        this.taxaService = taxaService;
        this.contratoMapper = contratoMapper;
        this.integracaoBufferService = integracaoBufferService;
    }

    @Override
    public Contrato salvar(final Contrato contrato) {

        if (contratoRepository.existsByEmpresaAndOperadora(contrato.getEmpresa(), contrato.getOperadora())) {
            throw new NotificacaoException("Já existe um contrato para a empresa e operadora selecionados.");
        }

        validaTaxas(contrato.getTaxas());

        adicionaIntegracaoBuffer(contrato);

        return contratoRepository.save(contrato);
    }

    @Override
    public List<Contrato> pesquisar(final Long empresaId, final Long operadoraId) {

        List<Contrato> contratos;
        var operadora = new Operadora(operadoraId);
        var empresa = new Empresa(empresaId);

        if (empresaId != null && operadoraId != null) {
            contratos = contratoRepository.findByEmpresaAndOperadora(empresa, operadora);
        } else if (empresaId != null) {
            contratos = contratoRepository.findByEmpresa(empresa);
        } else if (operadoraId != null) {
            contratos = contratoRepository.findByOperadora(operadora);
        } else {
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

    @Override
    public Contrato ativar(Long id) {
        Contrato contrato = this.pesquisarPorId(id);
        contrato.setAtivo(true);
        contrato.getTaxas().forEach(taxa -> taxa.setAtivo(contrato.getAtivo()));
        contratoRepository.save(contrato);

        return contrato;
    }

    @Override
    public Contrato desativar(Long id) {
        Contrato contrato = this.pesquisarPorId(id);
        contrato.setAtivo(false);
        contrato.getTaxas().forEach(taxa -> taxa.setAtivo(contrato.getAtivo()));
        contratoRepository.save(contrato);

        return contrato;
    }

    private void validaTaxas(final List<Taxa> taxas) {
        taxaService.verificaDuplicidade(taxas);
        taxaService.validaEntraEmVigor(taxas);
        taxas.forEach(taxaService::validar);
    }

    private void adicionaIntegracaoBuffer(final Contrato contrato) {
        IntegracaoBuffer integracaoBuffer = new IntegracaoBuffer();
        integracaoBuffer.setNomeEmpresa(contrato.getEmpresa().getRazaoSocial());
        integracaoBuffer.setNomeOperadora(contrato.getOperadora().getDescricao());
        integracaoBuffer.setCnpj(somenteNumero(contrato.getEmpresa().getCnpj()));
        integracaoBuffer.setDataHora(LocalDateTime.now());
        integracaoBufferService.salvar(integracaoBuffer);
    }
}

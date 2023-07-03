package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.assembler.impl.TaxaMapper;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Taxa;
import com.ctsousa.econcilia.model.dto.OperadoraDTO;
import com.ctsousa.econcilia.repository.OperadoraRepository;
import com.ctsousa.econcilia.service.OperadoraService;
import com.ctsousa.econcilia.service.TaxaService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class OperadoraServiceImpl implements OperadoraService  {
    private final OperadoraRepository operadoraRepository;
    private final TaxaService taxaService;
    private final TaxaMapper taxaMapper;

    public OperadoraServiceImpl(OperadoraRepository operadoraRepository, TaxaService taxaService, TaxaMapper taxaMapper) {
        this.operadoraRepository = operadoraRepository;
        this.taxaService = taxaService;
        this.taxaMapper = taxaMapper;
    }

    @Override
    public Operadora buscarPorID(Long id) {
        Operadora operadora = operadoraRepository.porID(id);
        if (operadora == null) {
            throw new NotificacaoException("Operadora não encontrada");
        }
        return operadora;
    }

    @Override
    public List<Operadora> pesquisar(String descricao) {
        if (descricao != null) {
            return operadoraRepository.porDescricao(descricao);
        }
        return operadoraRepository.findAll();
    }

    @Override
    public Operadora salvar (Operadora operadora) {
        validar(operadora);
        temTaxaDuplicada(operadora.getTaxas());
        podeEntrarEmVigor(operadora.getTaxas());
        return operadoraRepository.save(operadora);
    }

    @Override
    public Operadora atualizar(Long id, OperadoraDTO operadoraDTO) {
        Operadora operadora = buscarPorID(id);

        List<Taxa> taxas = operadoraDTO.getTaxas().stream()
                        .map(taxaMapper::paraEntidade)
                        .toList();

        operadora.setTaxas(new ArrayList<>());
        taxas.forEach(operadora::adicionaTaxa);

        BeanUtils.copyProperties(operadoraDTO, operadora, "id");
        return operadoraRepository.save(operadora);
    }

    @Override
    public void deletar(Long id) {
        var operadora = operadoraRepository.findById(id);
        operadora.ifPresent(operadoraRepository::delete);
    }

    private void validar (final Operadora operadora) {
        if (operadoraRepository.existsByDescricao(operadora.getDescricao())) {
            throw new NotificacaoException("Essa operadora já existe.");
        }
        if (operadora.getTaxas().isEmpty()) {
            throw new NotificacaoException("Informe pelo menos uma taxa.");
        }
    }

    private void temTaxaDuplicada(final List<Taxa> taxas) {
        taxaService.verificaDuplicidade(taxas);
    }

    private void podeEntrarEmVigor(final List<Taxa> taxas) {
       taxaService.validaEntraEmVigor(taxas);
    }
}

package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.dto.OperadoraDTO;
import com.ctsousa.econcilia.repository.OperadoraRepository;
import com.ctsousa.econcilia.service.OperadoraService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OperadoraServiceImpl implements OperadoraService {

    private final OperadoraRepository operadoraRepository;

    public OperadoraServiceImpl(OperadoraRepository operadoraRepository) {
        this.operadoraRepository = operadoraRepository;
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
    public Operadora salvar(Operadora operadora) {

        if (operadora == null) {
            throw new NotificacaoException("Operadora não informada.");
        }

        if (operadora.getDescricao() != null && operadoraRepository.existsByDescricao(operadora.getDescricao())) {
            throw new NotificacaoException("Já existe uma operadora com a descrição " + operadora.getDescricao());
        }

        return operadoraRepository.save(operadora);
    }

    @Override
    public Operadora atualizar(Long id, OperadoraDTO operadoraDTO) {
        Operadora operadora = buscarPorID(id);
        BeanUtils.copyProperties(operadoraDTO, operadora, "id");

        return operadoraRepository.save(operadora);
    }

    @Override
    public void deletar(Long id) {
        try {
            var operadora = operadoraRepository.findById(id);
            operadora.ifPresent(operadoraRepository::delete);
        } catch (Exception e) {
            throw new NotificacaoException("Operadora não pode ser excluída, pois já está associada a um contrado.");
        }
    }
}

package com.ctsousa.econcilia.assembler.impl;

import com.ctsousa.econcilia.assembler.ColecaoMapper;
import com.ctsousa.econcilia.assembler.DtoMapper;
import com.ctsousa.econcilia.assembler.EntidadeMapper;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.dto.OperadoraDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OperadoraMapper implements EntidadeMapper<Operadora, OperadoraDTO>, DtoMapper<Operadora, OperadoraDTO>, ColecaoMapper<Operadora, OperadoraDTO> {
    @Override
    public Operadora paraEntidade(OperadoraDTO dto) {
        Operadora operadora = new Operadora();
        operadora.setId(dto.getId());
        operadora.setDescricao(dto.getDescricao());
        operadora.setAtivo(dto.getAtivo());
        return operadora;
    }
    @Override
    public OperadoraDTO paraDTO(Operadora entidade) {
        OperadoraDTO operadoraDTO = new OperadoraDTO();
        operadoraDTO.setId(entidade.getId());
        operadoraDTO.setDescricao(entidade.getDescricao());
        operadoraDTO.setAtivo(entidade.getAtivo());
        return operadoraDTO;
    }

    @Override
    public List<OperadoraDTO> paraLista(List<Operadora> operadoras) {
        return operadoras.stream()
                .map(this::paraDTO)
                .collect(Collectors.toList());
    }
}

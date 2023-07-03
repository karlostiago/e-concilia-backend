package com.ctsousa.econcilia.assembler.impl;

import com.ctsousa.econcilia.assembler.ColecaoMapper;
import com.ctsousa.econcilia.assembler.DtoMapper;
import com.ctsousa.econcilia.assembler.EntidadeMapper;
import com.ctsousa.econcilia.model.Contrato;
import com.ctsousa.econcilia.model.dto.ContratoDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContratoMapper implements EntidadeMapper<Contrato, ContratoDTO>, DtoMapper<Contrato, ContratoDTO>, ColecaoMapper<Contrato, ContratoDTO> {

    private final EmpresaMapper empresaMapper;
    private final OperadoraMapper operadoraMapper;

    public ContratoMapper(EmpresaMapper empresaMapper, OperadoraMapper operadoraMapper) {
        this.empresaMapper = empresaMapper;
        this.operadoraMapper = operadoraMapper;
    }


    @Override
    public List<ContratoDTO> paraLista(List<Contrato> contratos) {
        return contratos.stream()
                .map(this::paraDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ContratoDTO paraDTO(Contrato contrato) {
        ContratoDTO contratoDTO = new ContratoDTO();
        contratoDTO.setEmpresa(empresaMapper.paraDTO(contrato.getEmpresa()));
        contratoDTO.setOperadora(operadoraMapper.paraDTO(contrato.getOperadora()));
        contratoDTO.setAtivo(contrato.getAtivo());
        return contratoDTO;
    }

    @Override
    public Contrato paraEntidade(ContratoDTO contratoDTO) {
        Contrato contrato = new Contrato();
        contrato.setEmpresa(empresaMapper.paraEntidade(contratoDTO.getEmpresa()));
        contrato.setOperadora(operadoraMapper.paraEntidade(contratoDTO.getOperadora()));
        contrato.setAtivo(contratoDTO.getAtivo());
        return contrato;
    }
}

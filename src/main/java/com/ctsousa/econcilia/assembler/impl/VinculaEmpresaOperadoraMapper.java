package com.ctsousa.econcilia.assembler.impl;

import com.ctsousa.econcilia.assembler.ColecaoMapper;
import com.ctsousa.econcilia.assembler.DtoMapper;
import com.ctsousa.econcilia.assembler.EntidadeMapper;
import com.ctsousa.econcilia.model.VinculaEmpresaOperadora;
import com.ctsousa.econcilia.model.dto.VinculaEmpresaOperadoraDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VinculaEmpresaOperadoraMapper implements EntidadeMapper<VinculaEmpresaOperadora, VinculaEmpresaOperadoraDTO>, DtoMapper<VinculaEmpresaOperadora, VinculaEmpresaOperadoraDTO>, ColecaoMapper<VinculaEmpresaOperadora, VinculaEmpresaOperadoraDTO> {

    private final EmpresaMapper empresaMapper;

    private final OperadoraMapper operadoraMapper;

    public VinculaEmpresaOperadoraMapper(EmpresaMapper empresaMapper, OperadoraMapper operadoraMapper) {
        this.empresaMapper = empresaMapper;
        this.operadoraMapper = operadoraMapper;
    }

    @Override
    public List<VinculaEmpresaOperadoraDTO> paraLista(List<VinculaEmpresaOperadora> list) {
        return list.stream()
                .map(this::paraDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VinculaEmpresaOperadoraDTO paraDTO(VinculaEmpresaOperadora entidade) {
        VinculaEmpresaOperadoraDTO vinculaEmpresaOperadoraDTO = new VinculaEmpresaOperadoraDTO();
        vinculaEmpresaOperadoraDTO.setId(entidade.getId());
        vinculaEmpresaOperadoraDTO.setCodigoIntegracao(entidade.getCodigoIntegracao());
        vinculaEmpresaOperadoraDTO.setEmpresa(empresaMapper.paraDTO(entidade.getEmpresa()));
        vinculaEmpresaOperadoraDTO.setOperadora(operadoraMapper.paraDTO(entidade.getOperadora()));
        return vinculaEmpresaOperadoraDTO;
    }

    @Override
    public VinculaEmpresaOperadora paraEntidade(VinculaEmpresaOperadoraDTO dto) {
        VinculaEmpresaOperadora vinculaEmpresaOperadora = new VinculaEmpresaOperadora();
        vinculaEmpresaOperadora.setId(dto.getId());
        vinculaEmpresaOperadora.setCodigoIntegracao(dto.getCodigoIntegracao());
        vinculaEmpresaOperadora.setEmpresa(empresaMapper.paraEntidade(dto.getEmpresa()));
        vinculaEmpresaOperadora.setOperadora(operadoraMapper.paraEntidade(dto.getOperadora()));
        return vinculaEmpresaOperadora;
    }
}

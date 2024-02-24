package com.ctsousa.econcilia.mapper.impl;

import com.ctsousa.econcilia.Mapper;
import com.ctsousa.econcilia.mapper.ColecaoMapper;
import com.ctsousa.econcilia.mapper.DtoMapper;
import com.ctsousa.econcilia.mapper.EntidadeMapper;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.dto.IntegracaoDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public class IntegracaoMapper implements EntidadeMapper<Integracao, IntegracaoDTO>, DtoMapper<Integracao, IntegracaoDTO>, ColecaoMapper<Integracao, IntegracaoDTO> {

    private final EmpresaMapper empresaMapper;

    private final OperadoraMapper operadoraMapper;

    public IntegracaoMapper(EmpresaMapper empresaMapper, OperadoraMapper operadoraMapper) {
        this.empresaMapper = empresaMapper;
        this.operadoraMapper = operadoraMapper;
    }

    @Override
    public List<IntegracaoDTO> paraLista(List<Integracao> list) {
        return list.stream()
                .map(this::paraDTO)
                .toList();
    }

    @Override
    public IntegracaoDTO paraDTO(Integracao entidade) {
        IntegracaoDTO integracaoDTO = new IntegracaoDTO();
        integracaoDTO.setId(entidade.getId());
        integracaoDTO.setCodigoIntegracao(entidade.getCodigoIntegracao());
        integracaoDTO.setEmpresa(empresaMapper.paraDTO(entidade.getEmpresa()));
        integracaoDTO.setOperadora(operadoraMapper.paraDTO(entidade.getOperadora()));
        return integracaoDTO;
    }

    @Override
    public Integracao paraEntidade(IntegracaoDTO dto) {
        Integracao integracao = new Integracao();
        integracao.setId(dto.getId());
        integracao.setCodigoIntegracao(dto.getCodigoIntegracao());
        integracao.setEmpresa(empresaMapper.paraEntidade(dto.getEmpresa()));
        integracao.setOperadora(operadoraMapper.paraEntidade(dto.getOperadora()));
        return integracao;
    }
}

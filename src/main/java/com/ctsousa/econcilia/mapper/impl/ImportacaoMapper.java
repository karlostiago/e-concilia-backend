package com.ctsousa.econcilia.mapper.impl;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import com.ctsousa.econcilia.enumaration.ImportacaoSituacao;
import com.ctsousa.econcilia.mapper.ColecaoMapper;
import com.ctsousa.econcilia.mapper.DtoMapper;
import com.ctsousa.econcilia.mapper.EntidadeMapper;
import com.ctsousa.econcilia.model.Importacao;
import com.ctsousa.econcilia.model.dto.ImportacaoDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ExcludedCoverage
public class ImportacaoMapper implements EntidadeMapper<Importacao, ImportacaoDTO>, DtoMapper<Importacao, ImportacaoDTO>, ColecaoMapper<Importacao, ImportacaoDTO> {

    @Override
    public List<ImportacaoDTO> paraLista(List<Importacao> importacoes) {
        return importacoes.stream()
                .map(this::paraDTO)
                .toList();
    }

    @Override
    public ImportacaoDTO paraDTO(Importacao importacao) {
        ImportacaoDTO importacaoDTO = new ImportacaoDTO();
        importacaoDTO.setDataInicial(importacao.getDataInicial());
        importacaoDTO.setDataFinal(importacao.getDataFinal());
        importacaoDTO.setEmpresa(importacao.getEmpresa());
        importacaoDTO.setOperadora(importacao.getOperadora());
        importacaoDTO.setSituacao(importacao.getSituacao().getDescricao().toUpperCase());
        return importacaoDTO;
    }

    @Override
    public Importacao paraEntidade(ImportacaoDTO importacaoDTO) {
        Importacao importacao = new Importacao();
        importacao.setDataInicial(importacaoDTO.getDataInicial());
        importacao.setDataFinal(importacaoDTO.getDataFinal());
        importacao.setEmpresa(importacaoDTO.getEmpresa());
        importacao.setOperadora(importacaoDTO.getOperadora());
        importacao.setSituacao(ImportacaoSituacao.porDescricao(importacaoDTO.getSituacao()));
        return importacao;
    }
}

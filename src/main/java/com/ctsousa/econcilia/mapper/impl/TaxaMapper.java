package com.ctsousa.econcilia.mapper.impl;

import com.ctsousa.econcilia.mapper.ColecaoMapper;
import com.ctsousa.econcilia.mapper.DtoMapper;
import com.ctsousa.econcilia.mapper.EntidadeMapper;
import com.ctsousa.econcilia.model.Taxa;
import com.ctsousa.econcilia.model.dto.TaxaDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaxaMapper implements EntidadeMapper<Taxa, TaxaDTO>, DtoMapper<Taxa, TaxaDTO>, ColecaoMapper<Taxa, TaxaDTO> {

    @Override
    public Taxa paraEntidade(TaxaDTO taxaDTO) {
        Taxa taxa = new Taxa();
        taxa.setId(taxaDTO.getId());
        taxa.setDescricao(taxaDTO.getDescricao());
        taxa.setValor(taxaDTO.getValor());
        taxa.setValidoAte(taxaDTO.getValidoAte());
        taxa.setEntraEmVigor(taxaDTO.getEntraEmVigor());
        taxa.setAtivo(taxaDTO.getAtivo());
        return taxa;
    }

    @Override
    public TaxaDTO paraDTO(Taxa taxa) {
        TaxaDTO taxaDTO = new TaxaDTO();
        taxaDTO.setId(taxa.getId());
        taxaDTO.setDescricao(taxa.getDescricao());
        taxaDTO.setValor(taxa.getValor());
        taxaDTO.setValidoAte(taxa.getValidoAte());
        taxaDTO.setEntraEmVigor(taxa.getEntraEmVigor());
        taxaDTO.setAtivo(taxa.getAtivo());
        taxaDTO.setEmpresa(taxa.getContrato().getEmpresa().getRazaoSocial());
        return taxaDTO;
    }

    @Override
    public List<TaxaDTO> paraLista(List<Taxa> taxas) {
        return taxas.stream()
                .map(this::paraDTO)
                .toList();
    }
}

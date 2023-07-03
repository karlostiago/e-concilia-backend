package com.ctsousa.econcilia.assembler.impl;

import com.ctsousa.econcilia.assembler.EntidadeMapper;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Taxa;
import com.ctsousa.econcilia.model.dto.TaxaDTO;
import org.springframework.stereotype.Component;

@Component
public class TaxaMapper implements EntidadeMapper<Taxa, TaxaDTO> {
    @Override
    public Taxa paraEntidade(TaxaDTO taxaDTO) {
        Taxa taxa = new Taxa();
        taxa.setDescricao(taxaDTO.getDescricao());
        taxa.setValor(taxaDTO.getValor());
        taxa.setValidoAte(taxaDTO.getValidoAte());
        taxa.setEntraEmVigor(taxaDTO.getEntraEmVigor());
        taxa.setAtivo(taxaDTO.getAtivo());
        return taxa;
    }
}

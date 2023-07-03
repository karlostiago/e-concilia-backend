package com.ctsousa.econcilia.assembler.impl;

import com.ctsousa.econcilia.assembler.ColecaoMapper;
import com.ctsousa.econcilia.assembler.DtoMapper;
import com.ctsousa.econcilia.assembler.EntidadeMapper;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Taxa;
import com.ctsousa.econcilia.model.dto.EmpresaDTO;
import com.ctsousa.econcilia.model.dto.OperadoraDTO;
import com.ctsousa.econcilia.model.dto.TaxaDTO;
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

        List<Taxa> taxas = dto.getTaxas().stream()
                .map(taxaDTO -> {
                    Taxa taxa = new Taxa();
                    taxa.setDescricao(taxaDTO.getDescricao());
                    taxa.setValor(taxaDTO.getValor());
                    taxa.setValidoAte(taxaDTO.getValidoAte());
                    taxa.setEntraEmVigor(taxaDTO.getEntraEmVigor());
                    taxa.setAtivo(taxaDTO.getAtivo());
                    taxa.setOperadora(operadora);
                    return taxa;
                })
                .toList();

        taxas.forEach(operadora::adicionaTaxa);

        return operadora;
    }
    @Override
    public OperadoraDTO paraDTO(Operadora entidade) {
        OperadoraDTO operadoraDTO = new OperadoraDTO();
        operadoraDTO.setId(entidade.getId());
        operadoraDTO.setDescricao(entidade.getDescricao());
        operadoraDTO.setAtivo(entidade.getAtivo());

        List<TaxaDTO> taxas = entidade.getTaxas().stream()
                .map(taxa -> {
                    TaxaDTO taxaDTO = new TaxaDTO();
                    taxaDTO.setDescricao(taxa.getDescricao());
                    taxaDTO.setValor(taxa.getValor());
                    taxaDTO.setAtivo(taxa.getAtivo());
                    taxaDTO.setValidoAte(taxa.getValidoAte());
                    taxaDTO.setEntraEmVigor(taxa.getEntraEmVigor());
                    taxaDTO.setExpiraEm(taxa.expiraEm());
                    taxaDTO.setOperadora(operadoraDTO);
                    return taxaDTO;
                })
                .toList();

       operadoraDTO.setTaxas(taxas);

        return operadoraDTO;
    }

    @Override
    public List<OperadoraDTO> paraLista(List<Operadora> operadoras) {
        return operadoras.stream()
                .map(this::paraDTO)
                .collect(Collectors.toList());
    }
}

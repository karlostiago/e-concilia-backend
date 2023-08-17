package com.ctsousa.econcilia.assembler.impl;

import com.ctsousa.econcilia.assembler.ColecaoMapper;
import com.ctsousa.econcilia.assembler.DtoMapper;
import com.ctsousa.econcilia.assembler.EntidadeMapper;
import com.ctsousa.econcilia.model.Contrato;
import com.ctsousa.econcilia.model.Taxa;
import com.ctsousa.econcilia.model.dto.ContratoDTO;
import com.ctsousa.econcilia.model.dto.TaxaDTO;
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
        contratoDTO.setNumero(contrato.getId());
        contratoDTO.setEmpresa(empresaMapper.paraDTO(contrato.getEmpresa()));
        contratoDTO.setOperadora(operadoraMapper.paraDTO(contrato.getOperadora()));
        contratoDTO.setAtivo(contrato.getAtivo());

        List<TaxaDTO> taxas = contrato.getTaxas().stream()
                .map(taxa -> {
                    TaxaDTO taxaDTO = new TaxaDTO();
                    taxaDTO.setDescricao(taxa.getDescricao());
                    taxaDTO.setValor(taxa.getValor());
                    taxaDTO.setAtivo(taxa.getAtivo());
                    taxaDTO.setValidoAte(taxa.getValidoAte());
                    taxaDTO.setEntraEmVigor(taxa.getEntraEmVigor());
                    taxaDTO.setExpiraEm(taxa.expiraEm());
                    taxaDTO.setContrato(contratoDTO);
                    return taxaDTO;
                })
                .toList();

        contratoDTO.setTaxas(taxas);

        return contratoDTO;
    }

    @Override
    public Contrato paraEntidade(ContratoDTO contratoDTO) {
        Contrato contrato = new Contrato();
        contrato.setId(contratoDTO.getNumero());
        contrato.setEmpresa(empresaMapper.paraEntidade(contratoDTO.getEmpresa()));
        contrato.setOperadora(operadoraMapper.paraEntidade(contratoDTO.getOperadora()));
        contrato.setAtivo(contratoDTO.getAtivo());

        List<Taxa> taxas = contratoDTO.getTaxas().stream()
                .map(taxaDTO -> {
                    Taxa taxa = new Taxa();
                    taxa.setDescricao(taxaDTO.getDescricao());
                    taxa.setValor(taxaDTO.getValor());
                    taxa.setValidoAte(taxaDTO.getValidoAte());
                    taxa.setEntraEmVigor(taxaDTO.getEntraEmVigor());
                    taxa.setAtivo(taxaDTO.getAtivo());
                    taxa.setContrato(contrato);
                    return taxa;
                })
                .toList();

        taxas.forEach(contrato::adicionaTaxa);

        return contrato;
    }
}

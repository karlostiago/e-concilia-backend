package com.ctsousa.econcilia.mapper.impl;

import com.ctsousa.econcilia.mapper.EntidadeMapper;
import com.ctsousa.econcilia.model.Consolidado;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.dto.ConsolidadoDTO;
import org.springframework.stereotype.Component;

@Component
public class ConsolidadoMapper implements EntidadeMapper<Consolidado, ConsolidadoDTO> {

    @Override
    public Consolidado paraEntidade(ConsolidadoDTO dto) {
        Consolidado consolidado = new Consolidado();
        consolidado.setEmpresa(new Empresa(dto.getEmpresaId()));
        consolidado.setOperadora(new Operadora(dto.getOperadoraId()));
        consolidado.setPeriodo(dto.getPeriodo());
        consolidado.setTotalBruto(dto.getTotalBruto());
        consolidado.setTotalLiquido(dto.getTotalLiquido());
        consolidado.setQuantidadeVenda(dto.getQuantidadeVenda());
        consolidado.setTicketMedio(dto.getTicketMedio());
        consolidado.setTotalCancelado(dto.getTotalCancelado());
        consolidado.setTotalRecebido(dto.getTotalRecebido());
        consolidado.setTotalTaxaEntrega(dto.getTotalTaxaEntrega());
        consolidado.setTotalComissao(dto.getTotalComissao());
        consolidado.setTotalPromocao(dto.getTotalPromocao());
        consolidado.setTotalTransacaoPagamento(dto.getTotalTransacaoPagamento());
        consolidado.setTotalTaxaServico(dto.getTotalTaxaServico());
        consolidado.setTotalRepasse(dto.getTotalRepasse());
        consolidado.setTotalTaxaManutencao(dto.getTotalTaxaManutencao());
        return consolidado;
    }
}

package com.ctsousa.econcilia.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelatorioConsolidadoDTO {

    private String periodo;
    private String totalBruto;
    private String quantidadeVenda;
    private String valorAntecipado;
    private String ticketMedio;
    private String totalCancelado;
    private String totalTaxaEntrega;
    private String totalComissao;
    private String totalPromocao;
    private String totalTransacaoPagamento;
    private String totalTaxaServico;
    private String totalRepasse;
    private String totalTaxaManutencao;
    private RelatorioDTO.Info info;
}


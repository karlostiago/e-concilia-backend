package com.ctsousa.econcilia.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelatorioVendaDTO {
    private String dataPedido;
    private String numeroDocumento;
    private String razaoSocial;
    private String formaPagamento;
    private String responsavel;
    private String valorBruto;
    private String valorParcial;
    private String valorCancelado;
    private String valorComissao;
    private String valorTaxaEntrega;
    private String valorTaxaServico;
    private String taxaComissao;
    private String taxaComissaoPagamento;
    private RelatorioDTO.Info info;
}

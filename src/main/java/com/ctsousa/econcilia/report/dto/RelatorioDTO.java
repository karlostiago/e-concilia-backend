package com.ctsousa.econcilia.report.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class RelatorioDTO {

    private final List<RelatorioConsolidadoDTO> consolidados;
    private final List<RelatorioTaxaDTO> taxas;
    private final List<RelatorioVendaDTO> vendas;

    public RelatorioDTO(List<RelatorioConsolidadoDTO> consolidados, List<RelatorioTaxaDTO> taxas, List<RelatorioVendaDTO> vendas) {
        this.consolidados = consolidados;
        this.taxas = taxas;
        this.vendas = vendas;
    }

    @Getter
    @Setter
    public static class Info {
        private String titulo;
        private String nome;
        private String endereco;
        private String telefone;
        private String email;
    }
}

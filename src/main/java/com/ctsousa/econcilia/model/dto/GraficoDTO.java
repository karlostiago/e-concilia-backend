package com.ctsousa.econcilia.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GraficoDTO {

    private GraficoVendaUltimo7DiaDTO graficoVendaUltimo7DiaDTO;
    private GraficoVendaMensalDTO graficoVendaMensalDTO;
    private GraficoVendaAnualDTO graficoVendaAnualDTO;
    private GraficoPercentualVendaFormaPagamentoDTO graficoPercentualVendaFormaPagamentoDTO;
    private GraficoVendaUltimo7DiaCreditoDebitoDTO graficoVendaUltimo7DiaCreditoDebitoDTO;
    private GraficoVendaUltimo7DiaDinheiroPixDTO graficoVendaUltimo7DiaDinheiroPixDTO;
}

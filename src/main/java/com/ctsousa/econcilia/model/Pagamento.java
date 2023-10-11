package com.ctsousa.econcilia.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class Pagamento {

    private String periodoId;

    private String status;

    private LocalDate dataExecucaoEsperada;

    private LocalDate proximaDataExecucao;

    private LocalDate dataConfirmacaoPagamento;

    private BigDecimal valorTotal;

    private String codigoTransacao;

    private String tipo;
}

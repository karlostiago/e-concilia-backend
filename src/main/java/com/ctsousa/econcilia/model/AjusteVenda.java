package com.ctsousa.econcilia.model;

import com.ctsousa.econcilia.Model;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Model
public class AjusteVenda {

    private String pedidoId;

    private String pedidoCobrancaId;

    private String periodoId;

    private String numeroDocumento;

    private LocalDate dataPedido;

    private LocalDate dataPedidoAtualizado;

    private BigDecimal valorAjuste = new BigDecimal("0.0");

    private LocalDate dataPagamentoEsperada;

    private Cobranca cobranca;
}

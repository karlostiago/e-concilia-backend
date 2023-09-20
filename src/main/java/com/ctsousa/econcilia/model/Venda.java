package com.ctsousa.econcilia.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class Venda {

    private LocalDate dataPedido;

    private String pedidoId;

    private LocalDate ultimaDataProcessamento;

    private String razaoSocial;

    private String numeroDocumento;

    private String modeloNegocio;

    private Pagamento pagamento;

    private Cobranca cobranca;

    private Boolean conciliado;

    private BigDecimal diferenca;
}

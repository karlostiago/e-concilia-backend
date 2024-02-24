package com.ctsousa.econcilia.model;

import com.ctsousa.econcilia.Model;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Model
public class Cancelamento {

    private String nomeComerciante;

    private String comercianteId;

    private String pedidoId;

    private String periodoId;

    private BigDecimal valor;
}

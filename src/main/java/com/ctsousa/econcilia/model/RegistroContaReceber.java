package com.ctsousa.econcilia.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RegistroContaReceber {

    private String id;

    private String origemPagamentoId;

    private List<DivisaoPagamento> pagamentos;
}

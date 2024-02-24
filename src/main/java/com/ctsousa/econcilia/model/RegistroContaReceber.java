package com.ctsousa.econcilia.model;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ExcludedCoverage
public class RegistroContaReceber {

    private String id;

    private String origemPagamentoId;

    private List<DivisaoPagamento> pagamentos;
}

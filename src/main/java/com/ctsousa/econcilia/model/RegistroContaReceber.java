package com.ctsousa.econcilia.model;

import com.ctsousa.econcilia.Model;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Model
public class RegistroContaReceber {

    private String id;

    private String origemPagamentoId;

    private List<DivisaoPagamento> pagamentos;
}

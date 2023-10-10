package com.ctsousa.econcilia.model.dto;

import com.ctsousa.econcilia.model.Venda;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ConciliadorDTO {

    private List<Venda> vendas;

    private TotalizadorDTO totalizador;

    private ResumoFinanceiroDTO resumoFinanceiro;
}

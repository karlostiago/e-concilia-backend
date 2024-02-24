package com.ctsousa.econcilia.model.dto;

import com.ctsousa.econcilia.Dto;
import com.ctsousa.econcilia.model.Venda;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Dto
public class ConciliadorDTO {

    private List<Venda> vendas;

    private TotalizadorDTO totalizador;

    private ResumoFinanceiroDTO resumoFinanceiro;

    public ConciliadorDTO(final List<Venda> vendas) {
        this.vendas = vendas;
    }
}

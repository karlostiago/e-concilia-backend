package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.ResumoFinanceiroDTO;
import com.ctsousa.econcilia.model.dto.TotalizadorDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ConciliadorIfoodService {

    List<Venda> conciliarTaxas(final List<Venda> vendas, final String lojaId);

    void aplicarCancelamento(final List<Venda> vendas, final String lojaId);

    TotalizadorDTO totalizar(List<Venda> vendas);

    ResumoFinanceiroDTO calcularResumoFinanceiro(List<Venda> vendas);
}

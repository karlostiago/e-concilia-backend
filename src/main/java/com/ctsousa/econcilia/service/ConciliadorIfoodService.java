package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Ocorrencia;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.ConciliadorDTO;
import com.ctsousa.econcilia.model.dto.ResumoFinanceiroDTO;
import com.ctsousa.econcilia.model.dto.TotalizadorDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface ConciliadorIfoodService {

//    List<Venda> conciliarTaxas(final List<Venda> vendas, final String lojaId);
//
//    void aplicarCancelamento(final List<Venda> vendas, final String lojaId);
//
//    void reprocessarVenda(final LocalDate dtInicial, final LocalDate dtFinal, final String lojaId, List<Venda> vendas);
//
//    TotalizadorDTO totalizar(List<Venda> vendas);
//
//    TotalizadorDTO totalizar(List<Venda> vendas, List<Ocorrencia> ocorrencias);
//
//    ResumoFinanceiroDTO calcularResumoFinanceiro(List<Venda> vendas);

    ConciliadorDTO conciliar(final String codigoLoja, final String metodoPagamento, final String bandeira, final String tipoRecebimento, final LocalDate dtInicial, final LocalDate dtFinal);
}

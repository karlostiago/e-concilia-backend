package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.enumaration.MetodoPagamento;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Taxa;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.ResumoFinanceiroDTO;
import com.ctsousa.econcilia.model.dto.TotalizadorDTO;
import com.ctsousa.econcilia.service.ConciliadorIfoodService;
import com.ctsousa.econcilia.service.IntegracaoService;
import com.ctsousa.econcilia.service.TaxaService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ctsousa.econcilia.util.StringUtil.maiuscula;

@Component
public class ConciliadorIfoodServiceImpl implements ConciliadorIfoodService {

    private final TaxaService taxaService;

    private final IntegracaoService integracaoService;

    public ConciliadorIfoodServiceImpl(TaxaService taxaService, IntegracaoService integracaoService) {
        this.taxaService = taxaService;
        this.integracaoService = integracaoService;
    }

    @Override
    public List<Venda> conciliarTaxas(List<Venda> vendas, final String lojaId) {
        Integracao integracao = integracaoService.pesquisarPorCodigoIntegracao(lojaId);
        Empresa empresa = null;

        if (integracao == null) {
            throw new NotificacaoException("Não foi encontrada nenhuma empresa para o código integração.::: " + lojaId);
        }

        empresa = integracao.getEmpresa();
        List<Taxa> taxas = taxaService.buscarPorEmpresa(empresa.getId());

        for (Venda venda : vendas) {
            var taxa = buscarTaxa(taxas, maiuscula(venda.getPagamento().getTipo()));
            if (taxa != null) {
                calcularTaxaAdquirenteAplicada(venda, taxa);
            }
            var conciliado = venda.getCobranca().getTaxaAdquirente().add(venda.getCobranca().getTaxaAdquirenteAplicada()).setScale(1, RoundingMode.HALF_UP).equals(new BigDecimal("0.0"));
            venda.setConciliado(conciliado);
            calcularDiferenca(venda);
        }

        return vendas;
    }

    @Override
    public TotalizadorDTO totalizar(List<Venda> vendas) {
        TotalizadorDTO totalizadorDTO = new TotalizadorDTO();

        for (Venda venda : vendas) {
            var totalPedido = venda.getCobranca().getTotalItensPedido();
            var totalTaxaEntrega = venda.getCobranca().getTaxaEntrega();
            var totalTaxaPraticada = Math.abs(venda.getCobranca().getTaxaAdquirente().doubleValue());
            var totalTaxaAplicada = venda.getCobranca().getTaxaAdquirenteAplicada();
            var totalDiferenca = venda.getDiferenca();

            totalizadorDTO.setTotalValorPedido(totalizadorDTO.getTotalValorPedido().add(totalPedido));
            totalizadorDTO.setTotalTaxaEntrega(totalizadorDTO.getTotalTaxaEntrega().add(totalTaxaEntrega));
            totalizadorDTO.setTotalTaxaAquisicaoPraticada(totalizadorDTO.getTotalTaxaAquisicaoPraticada().add(BigDecimal.valueOf(totalTaxaPraticada)));
            totalizadorDTO.setTotalTaxaAquisicaoAplicada(totalizadorDTO.getTotalTaxaAquisicaoAplicada().add(totalTaxaAplicada));
            totalizadorDTO.setTotalDiferenca(totalizadorDTO.getTotalDiferenca().add(totalDiferenca));
        }

        return totalizadorDTO;
    }

    @Override
    public ResumoFinanceiroDTO calcularResumoFinanceiro(List<Venda> vendas) {
        Map<String, BigDecimal> totalPorMetodoPagamento = new HashMap<>();

        for (Venda venda : vendas) {
            String metodoPagamento = venda.getPagamento().getMetodo();
            BigDecimal total = totalPorMetodoPagamento.getOrDefault(metodoPagamento, new BigDecimal("0.0"));
            total = total.add(venda.getCobranca().getTotalItensPedido());
            totalPorMetodoPagamento.put(metodoPagamento, total);
        }

        ResumoFinanceiroDTO resumoFinanceiroDTO = new ResumoFinanceiroDTO();
        resumoFinanceiroDTO.setTotalCredito(totalPorMetodoPagamento.getOrDefault(MetodoPagamento.CREDIT.getDescricao(), new BigDecimal("0.0")));
        resumoFinanceiroDTO.setTotalDebito(totalPorMetodoPagamento.getOrDefault(MetodoPagamento.DEBIT.getDescricao(), new BigDecimal("0.0")));
        resumoFinanceiroDTO.setTotalPix(totalPorMetodoPagamento.getOrDefault(MetodoPagamento.PIX.getDescricao(), new BigDecimal("0.0")));
        resumoFinanceiroDTO.setTotalVoucher(totalPorMetodoPagamento.getOrDefault(MetodoPagamento.MEAL_VOUCHER.getDescricao(), new BigDecimal("0.0")));
        resumoFinanceiroDTO.setTotalBankPay(totalPorMetodoPagamento.getOrDefault(MetodoPagamento.BANK_PAY.getDescricao(), new BigDecimal("0.0")));
        resumoFinanceiroDTO.setTotalDinheiro(totalPorMetodoPagamento.getOrDefault(MetodoPagamento.CASH.getDescricao(), new BigDecimal("0.0")));
        resumoFinanceiroDTO.setTotalOutros(totalPorMetodoPagamento.getOrDefault(MetodoPagamento.OUTROS.getDescricao(), new BigDecimal("0.0")));

        return resumoFinanceiroDTO;
    }

    private Taxa buscarTaxa(final List<Taxa> taxas, final String tipoPagamento) {
        for (Taxa taxa : taxas) {
            if (taxa.getDescricao().contains(tipoPagamento) && Boolean.TRUE.equals(taxa.getAtivo())) {
                return taxa;
            }
        }
        return null;
    }

    private void calcularTaxaAdquirenteAplicada(final Venda venda, final Taxa taxa) {
        var desconto = venda.getCobranca().getBeneficioComercio();
        var vTotalLiquido = venda.getCobranca().getGmv().add(desconto);
        var vTotal = vTotalLiquido.multiply(taxa.getValor()).divide(new BigDecimal("100"), RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);

        venda.getCobranca().setTaxaAdquirenteAplicada(vTotal);
    }

    private void calcularDiferenca(final Venda venda) {
        var diferenca = venda.getCobranca().getTaxaAdquirente().add(venda.getCobranca().getTaxaAdquirenteAplicada()).setScale(2, RoundingMode.HALF_UP);
        venda.setDiferenca(diferenca);
    }
}

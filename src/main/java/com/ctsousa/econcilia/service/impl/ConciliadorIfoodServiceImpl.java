package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Taxa;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.service.ConciliadorIfoodService;
import com.ctsousa.econcilia.service.IntegracaoService;
import com.ctsousa.econcilia.service.TaxaService;
import com.ctsousa.econcilia.util.StringUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    private Taxa buscarTaxa(final List<Taxa> taxas, final String tipoPagamento) {
        for (Taxa taxa : taxas) {
            if (taxa.getDescricao().contains(tipoPagamento) && taxa.getAtivo()) {
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

package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.enumaration.TipoProcessador;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Taxa;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.ConciliadorDTO;
import com.ctsousa.econcilia.model.dto.TotalizadorDTO;
import com.ctsousa.econcilia.processor.FormaRecebimento;
import com.ctsousa.econcilia.processor.Processador;
import com.ctsousa.econcilia.processor.ProcessadorFiltro;
import com.ctsousa.econcilia.service.ConciliadorIfoodService;
import com.ctsousa.econcilia.service.IntegracaoService;
import com.ctsousa.econcilia.service.TaxaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static com.ctsousa.econcilia.util.StringUtil.maiuscula;
import static com.ctsousa.econcilia.util.StringUtil.temValor;

@Slf4j
@Component
public class ConciliadorIfoodServiceImpl implements ConciliadorIfoodService {

    private final TaxaService taxaService;

    private final IntegracaoService integracaoService;

    public ConciliadorIfoodServiceImpl(TaxaService taxaService, IntegracaoService integracaoService) {
        this.taxaService = taxaService;
        this.integracaoService = integracaoService;
    }

    @Override
    public ConciliadorDTO conciliar(String codigoLoja, String metodoPagamento, String bandeira, String tipoRecebimento, LocalDate dtInicial, LocalDate dtFinal) {
        Integracao integracao = integracaoService.pesquisarPorCodigoIntegracao(codigoLoja);

        ProcessadorFiltro processadorFiltro = new ProcessadorFiltro();
        processadorFiltro.setCartaoBandeira(Boolean.TRUE.equals(temValor(bandeira)) ? bandeira : null);
        processadorFiltro.setIntegracao(integracao);
        processadorFiltro.setDtInicial(dtInicial);
        processadorFiltro.setDtFinal(dtFinal);
        processadorFiltro.setFormaPagamento(Boolean.TRUE.equals(temValor(metodoPagamento)) ? metodoPagamento : null);
        processadorFiltro.setFormaRecebimento(FormaRecebimento.porDescricao(tipoRecebimento));

        Processador processador = TipoProcessador.porOperadora(integracao.getOperadora());
        processador.processar(processadorFiltro, true);
        var vendas = processador.getVendas();

        conciliarTaxas(vendas, integracao);

        ConciliadorDTO conciliadorDTO = new ConciliadorDTO(vendas);
        conciliadorDTO.setTotalizador(getTotalizadorDTO(processador));

        return conciliadorDTO;
    }

    private void conciliarTaxas(final List<Venda> vendas, final Integracao integracao) {
        Empresa empresa = null;

        if (integracao == null) {
            throw new NotificacaoException("Não foi encontrada nenhuma empresa para o código integração.::: ");
        }

        empresa = integracao.getEmpresa();
        List<Taxa> taxas = taxaService.buscarPorEmpresa(empresa.getId());

        for (Venda venda : vendas) {
            var taxa = buscarTaxaPagamento(taxas);
            if (taxa != null) {
                calcularTaxaAdquirenteAplicada(venda, taxa);
            }
            var conciliado = venda.getCobranca().getTaxaAdquirente().add(venda.getCobranca().getTaxaAdquirenteAplicada()).setScale(1, RoundingMode.HALF_UP).equals(BigDecimal.valueOf(0D));
            venda.setConciliado(conciliado);
            calcularDiferenca(venda);
        }
    }

    private Taxa buscarTaxaPagamento(final List<Taxa> taxas) {
        for (Taxa taxa : taxas) {
            if (maiuscula(taxa.getDescricao()).contains("PAGAMENTO") && Boolean.TRUE.equals(taxa.getAtivo())) {
                return taxa;
            }
        }
        return null;
    }

    private void calcularTaxaAdquirenteAplicada(final Venda venda, final Taxa taxa) {
        if (naoDeveCalcularTaxa(venda)) return;

        var desconto = venda.getCobranca().getBeneficioComercio();
        var vTotalLiquido = venda.getCobranca().getValorBruto().add(desconto);
        var vTotal = vTotalLiquido.multiply(taxa.getValor()).divide(new BigDecimal("100"), RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);

        venda.getCobranca().setTaxaAdquirenteAplicada(vTotal);
    }

    private boolean naoDeveCalcularTaxa(final Venda venda) {
        return venda.getCobranca().getTaxaAdquirente().doubleValue() == 0D;
    }

    private void calcularDiferenca(final Venda venda) {
        var diferenca = venda.getCobranca().getTaxaAdquirente().add(venda.getCobranca().getTaxaAdquirenteAplicada()).setScale(2, RoundingMode.HALF_UP);
        venda.setDiferenca(diferenca);
    }

    private TotalizadorDTO getTotalizadorDTO(Processador processador) {
        TotalizadorDTO totalizadorDTO = new TotalizadorDTO();
        totalizadorDTO.setTotalValorBruto(processador.getValorTotalBruto());
        totalizadorDTO.setTotalValorPedido(processador.getValorTotalPedido());
        totalizadorDTO.setTotalValorLiquido(processador.getValorTotalLiquido());
        totalizadorDTO.setTotalValorCancelado(processador.getValorTotalCancelado());
        return totalizadorDTO;
    }
}

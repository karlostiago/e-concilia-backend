package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.enumaration.FormaRecebimento;
import com.ctsousa.econcilia.enumaration.TipoProcessador;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.*;
import com.ctsousa.econcilia.model.dto.ConciliadorDTO;
import com.ctsousa.econcilia.model.dto.TotalizadorDTO;
import com.ctsousa.econcilia.processor.Processador;
import com.ctsousa.econcilia.processor.ProcessadorFiltro;
import com.ctsousa.econcilia.service.ConciliadorIfoodService;
import com.ctsousa.econcilia.service.IntegracaoService;
import com.ctsousa.econcilia.service.TaxaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.ctsousa.econcilia.util.CalculadoraUtil.multiplicar;
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

        ProcessadorFiltro processadorFiltro = getProcessadorFiltr(integracao, metodoPagamento, bandeira, tipoRecebimento, dtInicial, dtFinal);
        Processador processador = TipoProcessador.porOperadora(integracao.getOperadora());
        processador.processar(processadorFiltro, true);
        var vendas = processador.getVendas();

        conciliarTaxas(vendas, integracao);

        ConciliadorDTO conciliadorDTO = new ConciliadorDTO(vendas);
        conciliadorDTO.setTotalizador(getTotalizadorDTO(processador));

        return conciliadorDTO;
    }

    private ProcessadorFiltro getProcessadorFiltr(Integracao integracao, String metodoPagamento, String bandeira, String tipoRecebimento, LocalDate dtInicial, LocalDate dtFinal) {
        ProcessadorFiltro processadorFiltro = new ProcessadorFiltro();
        processadorFiltro.setCartaoBandeira(Boolean.TRUE.equals(temValor(bandeira)) ? bandeira : null);
        processadorFiltro.setIntegracao(integracao);
        processadorFiltro.setDtInicial(dtInicial);
        processadorFiltro.setDtFinal(dtFinal);
        processadorFiltro.setFormaPagamento(Boolean.TRUE.equals(temValor(metodoPagamento)) ? metodoPagamento : null);
        processadorFiltro.setFormaRecebimento(FormaRecebimento.porDescricao(tipoRecebimento));
        return processadorFiltro;
    }

    private void conciliarTaxas(final List<Venda> vendas, final Integracao integracao) {
        Empresa empresa;
        Operadora operadora;

        if (integracao == null) {
            throw new NotificacaoException("Não foi encontrada nenhuma empresa para o código integração.::: ");
        }

        empresa = integracao.getEmpresa();
        operadora = integracao.getOperadora();

        for (Venda venda : vendas) {
            var cobranca = venda.getCobranca();
            var taxaPagamento = buscarTaxa(empresa, operadora, cobranca.getTaxaComissaoAdquirente(), "PAGAMENTO");
            var taxaComissao = buscarTaxa(empresa, operadora, cobranca.getTaxaComissao(), "COMISS");

            if (multiplicar(cobranca.getTaxaComissaoAdquirente(), 100).compareTo(taxaPagamento) == 0
                    && multiplicar(cobranca.getTaxaComissao(), 100).compareTo(taxaComissao) == 0) {
                venda.setConciliado(true);
            }
        }
    }

    private BigDecimal buscarTaxa(Empresa empresa, Operadora operadora, BigDecimal valorTaxa, final String descricao) {
        Taxa taxa = new Taxa();
        taxa.setValor(BigDecimal.valueOf(0D));

        try {
            if (valorTaxa.compareTo(BigDecimal.ZERO) > 0) {
                taxa = taxaService.buscarPor(empresa, operadora, descricao.toUpperCase(), valorTaxa.multiply(BigDecimal.valueOf(100)));
            }

            return taxa.getValor();
        } catch (NotificacaoException e) {
            return BigDecimal.valueOf(0D);
        }
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

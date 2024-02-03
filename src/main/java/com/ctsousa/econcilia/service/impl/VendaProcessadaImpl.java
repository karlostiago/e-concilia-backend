package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.*;
import com.ctsousa.econcilia.service.TaxaService;
import com.ctsousa.econcilia.service.VendaProcessadaService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static com.ctsousa.econcilia.util.CalculadoraUtil.somar;

@Component
@Deprecated(forRemoval = true)
public class VendaProcessadaImpl implements VendaProcessadaService {

    private final TaxaService taxaService;

    public VendaProcessadaImpl(TaxaService taxaService) {
        this.taxaService = taxaService;
    }

    @Override
    public VendaProcessada processar(List<Venda> vendas, List<Ocorrencia> ocorrencias) {
        var empresa = vendas.get(0).getEmpresa();
        var operadora = vendas.get(0).getOperadora();

        var valorTotalRepasse = BigDecimal.valueOf(0D);
        var valorTotalBrutoParcial = BigDecimal.valueOf(0D);
        var valorTotalBruto = BigDecimal.valueOf(0D);

        var valorTotalComissao = BigDecimal.valueOf(0D);
        var valorTotalTransacaoPagamento = BigDecimal.valueOf(0D);
        var valorTotalRecebidoViaLoja = BigDecimal.valueOf(0D);
        var valorTotalPromocao = BigDecimal.valueOf(0D);
        var valorTotalPedidoMinimo = BigDecimal.valueOf(0D);
        var valorTotalReembolsoBeneficio = BigDecimal.valueOf(0D);
        var valorTotalTaxaEntrega = BigDecimal.valueOf(0D);
        var valorTotalCancelado = BigDecimal.valueOf(0D);

        for (Venda venda : vendas) {
            if (venda.getCobranca().getValorCancelado().compareTo(BigDecimal.valueOf(0)) != 0) continue;

            valorTotalReembolsoBeneficio = valorTotalReembolsoBeneficio.add(getValorReembolsoBeneficio(venda.getCobranca()));
            valorTotalPedidoMinimo = valorTotalPedidoMinimo.add(getValorPedidoMinimo(venda.getCobranca()));
            valorTotalPromocao = valorTotalPromocao.add(venda.getCobranca().getBeneficioComercio().abs());
            valorTotalComissao = calcularValorComissao(venda.getCobranca(), valorTotalComissao);
            valorTotalRecebidoViaLoja = valorTotalRecebidoViaLoja.add(getValorRecebido(venda, operadora));
            valorTotalTransacaoPagamento = valorTotalTransacaoPagamento.add(venda.getCobranca().getTaxaAdquirente());
            valorTotalRepasse = valorTotalRepasse.add(venda.getCobranca().getTotalCredito().subtract(venda.getCobranca().getTotalDebito()));
            valorTotalBrutoParcial = valorTotalBrutoParcial.add(venda.getValorBruto());
            valorTotalTaxaEntrega = valorTotalTaxaEntrega.add(venda.getCobranca().getTaxaEntrega());
            valorTotalCancelado = valorTotalCancelado.add(venda.getValorCancelado());
        }

        var valorTotalMensalidade = calcularTaxaMensalidade(empresa, valorTotalBrutoParcial);

        var valorTotalOcorrencia = somar(ocorrencias.stream()
                .map(Ocorrencia::getValor)
                .toList());

        var valorOutrosLancamentos = calcularOutrosLancamentos(vendas, ocorrencias, valorTotalBrutoParcial);

        valorTotalRepasse = valorTotalRepasse
                .subtract(valorTotalMensalidade)
                .subtract(valorTotalOcorrencia);

        valorTotalBruto = valorTotalBruto
                .add(valorTotalPromocao)
                .add(valorTotalTransacaoPagamento.abs())
                .add(valorTotalPedidoMinimo)
                .add(valorTotalReembolsoBeneficio)
                .add(valorTotalRecebidoViaLoja.abs())
                .add(valorTotalComissao.abs())
                .add(valorTotalRepasse)
                .add(valorOutrosLancamentos);

        return VendaProcessada.builder()
                .totalRepasse(valorTotalRepasse)
                .totalPromocao(valorTotalPromocao.multiply(BigDecimal.valueOf(-1D)))
                .totalComissao(valorTotalComissao)
                .totalComissaoTransacaoPagamento(valorTotalTransacaoPagamento)
                .totalRecebidoLoja(valorTotalRecebidoViaLoja)
                .totalBruto(valorTotalBruto)
                .quantidade(BigInteger.valueOf(vendas.size()))
                .totalTicketMedio(calcularTicketMedio(BigInteger.valueOf(vendas.size()), valorTotalBruto))
                .totalTaxaEntrega(valorTotalTaxaEntrega)
                .totalCancelado(valorTotalCancelado)
                .build();
    }

    @Override
    public VendaProcessada processar(List<Venda> vendas) {
        return processar(vendas, new ArrayList<>());
    }

    private BigDecimal calcularValorComissao(Cobranca cobranca, BigDecimal valorComissao) {
        if (cobranca.getComissao().compareTo(BigDecimal.valueOf(0D)) < 0) {
            valorComissao = valorComissao.add(cobranca.getComissao());
        }
        if (cobranca.getComissao().compareTo(BigDecimal.valueOf(0D)) > 0) {
            valorComissao = valorComissao.subtract(cobranca.getComissao());
        }
        return valorComissao;
    }

    private BigDecimal getValorRecebido(Venda venda, Operadora operadora) {

        if (!venda.getPagamento().getResponsavel().equalsIgnoreCase(operadora.getDescricao())) {
            return venda.getValorTotalPedido();
        }

        return BigDecimal.valueOf(0D);
    }

    private BigDecimal getValorPedidoMinimo(Cobranca cobranca) {
        if (cobranca.getTaxaServico() != null) {
            return cobranca.getTaxaServico();
        }
        return BigDecimal.valueOf(0D);
    }

    private BigDecimal getValorReembolsoBeneficio(Cobranca cobranca) {
        if (cobranca.getTaxaAdquirenteBeneficio() != null && cobranca.getTaxaAdquirenteBeneficio().compareTo(BigDecimal.valueOf(0D)) > 0) {
            return cobranca.getTaxaAdquirenteBeneficio();
        }
        return BigDecimal.valueOf(0D);
    }

    private BigDecimal calcularTicketMedio(BigInteger quantidade, BigDecimal valorBruto) {
        if (quantidade.equals(BigInteger.ZERO)) return BigDecimal.valueOf(0D);

        return valorBruto.divide(new BigDecimal(quantidade), RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularOutrosLancamentos(List<Venda> vendas, List<Ocorrencia> ocorrencias, BigDecimal totalBruto) {
        var valorReembolso = calcularTotalReembolso(vendas);
        var taxaMensalidade = calcularTaxaMensalidade(vendas.get(0).getEmpresa(), totalBruto);

        var totalOcorrencia = somar(ocorrencias.stream()
                .map(Ocorrencia::getValor)
                .toList());

        var taxaBeneficioAdquirente = somar(vendas.stream()
                .filter(venda -> venda.getCobranca().getTaxaAdquirenteBeneficio() != null)
                .filter(venda -> venda.getCobranca().getTaxaAdquirenteBeneficio().compareTo(BigDecimal.valueOf(0D)) > 0)
                .map(venda -> venda.getCobranca().getTaxaAdquirenteBeneficio())
                .toList());

        return taxaMensalidade
                .subtract(valorReembolso)
                .subtract(taxaBeneficioAdquirente)
                .add(totalOcorrencia);
    }

    private BigDecimal calcularTaxaMensalidade(Empresa empresa, BigDecimal totalBruto) {
        Taxa taxa = new Taxa();
        taxa.setValor(BigDecimal.valueOf(0D));

        try {
            if (totalBruto.compareTo(BigDecimal.valueOf(1800D)) > 0) {
                taxa = taxaService.buscarPorDescricaoEmpresa("MEN", empresa);
            }
        } catch(NotificacaoException e) {
            // Nao precisa de tratamento
        }

        return taxa.getValor();
    }

    private BigDecimal calcularTotalReembolso(List<Venda> vendas) {
        var vendasComTaxaServico = vendas.stream()
                .filter(venda -> venda.getValorCancelado().equals(BigDecimal.valueOf(0D)))
                .filter(venda -> venda.getCobranca().getTaxaServico() != null && venda.getCobranca().getTaxaServico().compareTo(BigDecimal.valueOf(0D)) > 0)
                .toList();

        var taxaReembolso = BigDecimal.valueOf(0D);

        for (Venda venda : vendasComTaxaServico) {
            BigDecimal valorReembolso = venda.getCobranca().getTotalCredito().remainder(BigDecimal.ONE);
            if (valorReembolso.compareTo(BigDecimal.valueOf(0.01D)) == 0) {
                taxaReembolso = taxaReembolso.add(BigDecimal.valueOf(0.01D));
            }
            else if (valorReembolso.compareTo(BigDecimal.valueOf(0.02D)) == 0) {
                taxaReembolso = taxaReembolso.add(BigDecimal.valueOf(0.02D));
            }
            else if (valorReembolso.compareTo(BigDecimal.valueOf(0.03D)) == 0) {
                taxaReembolso = taxaReembolso.add(BigDecimal.valueOf(0.03D));
            }
        }

        return taxaReembolso;
    }
}

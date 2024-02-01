package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.*;
import com.ctsousa.econcilia.service.TaxaService;
import com.ctsousa.econcilia.service.VendaProcessadaService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

import static com.ctsousa.econcilia.util.CalculadoraUtil.somar;

@Component
public class VendaProcessadaImpl implements VendaProcessadaService {

    private final TaxaService taxaService;

    public VendaProcessadaImpl(TaxaService taxaService) {
        this.taxaService = taxaService;
    }

    @Override
    public VendaProcessada processar(List<Venda> vendas, List<Ocorrencia> ocorrencias) {
        VendaProcessada venda = processar(vendas);

        var totalDebito = somar(ocorrencias.stream()
                .map(Ocorrencia::getValor)
                .toList());

        var taxaMensalidade = getTaxaMensalidade(vendas.get(0).getEmpresa(), venda.getTotalBruto());

        return VendaProcessada
                .builder()
                .totalRepasse(venda.getTotalRepasse()
                        .subtract(totalDebito)
                        .subtract(taxaMensalidade))
                .totalBruto(venda.getTotalBruto())
                .totalPedido(venda.getTotalPedido())
                .totalCancelado(venda.getTotalCancelado())
                .totalLiquido(venda.getTotalLiquido())
                .totalTaxaEntrega(venda.getTotalTaxaEntrega())
                .totalDesconto(venda.getTotalDesconto())
                .totalTicketMedio(venda.getTotalTicketMedio())
                .totalComissao(venda.getTotalComissao())
                .quantidade(BigInteger.valueOf(vendas.size()))
                .totalTaxas(venda.getTotalTaxas())
                .taxaMedia(venda.getTaxaMedia())
                .totalComissaoOperadora(venda.getTotalComissaoOperadora())
                .totalComissaoTransacaoPagamento(venda.getTotalComissaoTransacaoPagamento().multiply(BigDecimal.valueOf(-1D)))
//                .totalPromocaoLoja(venda.getTotalPromocaoLoja().multiply(BigDecimal.valueOf(-1D)))
                .totalRecebidoLoja(venda.getTotalRecebidoLoja())
                .totalRecebidoOperadora(venda.getTotalRecebidoOperadora())
                .taxaService(venda.getTaxaService())
                .totalPromocao(venda.getTotalPromocao().multiply(BigDecimal.valueOf(-1D)))
                .totalComissaoTransacaoPagamento(venda.getTotalComissaoTransacaoPagamento())
                .build();
    }

    @Override
    public VendaProcessada processar(List<Venda> vendas) {

        var totalTaxaServico = vendas.stream()
                .filter(venda -> venda.getValorCancelado().equals(BigDecimal.valueOf(0D)))
                .filter(venda -> venda.getCobranca().getTaxaServico() != null)
                .map(venda -> venda.getCobranca().getTaxaServico())
                .toList();

        var totalRepasse = vendas.stream()
                .filter(venda -> venda.getValorCancelado().equals(BigDecimal.valueOf(0D)))
                .map(venda -> venda.getCobranca().getTotalCredito().subtract(venda.getCobranca().getTotalDebito()))
                .toList();

        var totalComissaoDebito = vendas.stream()
                .filter(venda -> venda.getValorCancelado().equals(BigDecimal.valueOf(0D)))
                .filter(venda -> venda.getCobranca().getComissao().compareTo(BigDecimal.valueOf(0D)) < 0)
                .map(venda -> venda.getCobranca().getComissao())
                .toList();

        var totalComissaoCredito = vendas.stream()
                .filter(venda -> venda.getValorCancelado().equals(BigDecimal.valueOf(0D)))
                .filter(venda -> venda.getCobranca().getComissao().compareTo(BigDecimal.valueOf(0D)) > 0)
                .map(venda -> venda.getCobranca().getComissao())
                .toList();

        var resultTotalComissaoDebito = somar(totalComissaoDebito);
        var resultTotalComissaoCredito = somar(totalComissaoCredito);
        var resultTotalComissao = resultTotalComissaoDebito.subtract(resultTotalComissaoCredito);
        var resultTotalRepasse = somar(totalRepasse);
        var resultTaxaServico = somar(totalTaxaServico);

        return VendaProcessada.builder()
                .totalRepasse(resultTotalRepasse)
                .totalBruto(calcularValorBruto(vendas))
                .totalPedido(calcularTotalPedido(vendas))
                .totalCancelado(calcularTotalCancelado(vendas))
                .totalLiquido(calcularTotalLiquido(vendas))
                .totalTaxaEntrega(calcularTotalTaxaEntrega(vendas))
                .totalDesconto(calcularTotalDesconto(vendas).multiply(BigDecimal.valueOf(-1D)))
                .totalTicketMedio(calcularTicketMedio(vendas))
                .totalComissao(resultTotalComissao)
                .quantidade(BigInteger.valueOf(vendas.size()))
                .totalTaxas(calcularTotalTaxas(vendas))
                .taxaService(resultTaxaServico)
                .totalPromocao(calcularIncentivoPromocionalLoja(vendas))
                .totalComissaoOperadora(resultTotalComissao)
                .totalComissaoTransacaoPagamento(calcularComissaoTransacaoPagamento(vendas))
                .totalRecebidoLoja(calcularValorRecebidoLoja(vendas))
                .totalRecebidoOperadora(calcularValorRecebidoOperadora(vendas))
                .build();
    }

    private BigDecimal calcularValorBruto(List<Venda> vendas) {
        var total = vendas.stream()
                .filter(venda -> venda.getCobranca().getValorCancelado().equals(BigDecimal.valueOf(0D)))
                .map(Venda::getValorBruto)
                .toList();

        return somar(total);
    }

    private BigDecimal calcularValorRecebidoOperadora(List<Venda> vendas) {
        var total = vendas.stream()
                .filter(venda -> venda.getPagamento().getResponsavel().equalsIgnoreCase("ifood"))
                .map(Venda::getValorTotalPedido)
                .toList();

        return somar(total);
    }

    private BigDecimal calcularValorRecebidoLoja(List<Venda> vendas) {
        var total = vendas.stream()
                .filter(venda -> !venda.getPagamento().getResponsavel().equalsIgnoreCase("ifood"))
                .map(Venda::getValorTotalPedido)
                .toList();

        return somar(total);
    }

    private BigDecimal calcularIncentivoPromocionalOperadora(List<Venda> vendas) {
        var total = vendas.stream()
                .filter(venda -> venda.getCobranca().getValorCancelado().equals(BigDecimal.valueOf(0D)))
                .map(venda -> venda.getCobranca().getBeneficioOperadora().abs())
                .toList();

        return somar(total);
    }

    private BigDecimal calcularIncentivoPromocionalLoja(List<Venda> vendas) {
        var total = vendas.stream()
                .filter(venda -> venda.getCobranca().getValorCancelado().equals(BigDecimal.valueOf(0D)))
                .map(venda -> venda.getCobranca().getBeneficioComercio().abs())
                .toList();

        return somar(total);
    }

    private BigDecimal calcularTotalPedido(List<Venda> vendas) {
        var total = vendas.stream()
                .map(Venda::getValorTotalPedido)
                .toList();

        return somar(total);
    }

    private BigDecimal calcularTotalLiquido(List<Venda> vendas) {
        var total = vendas.stream()
                .map(Venda::getValorLiquido)
                .toList();

        return somar(total);
    }

    private BigDecimal calcularTotalCancelado(List<Venda> vendas) {
        var total = vendas.stream()
                .map(Venda::getValorCancelado)
                .toList();

        return somar(total);
    }

    private BigDecimal calcularTotalTaxaEntrega(List<Venda> vendas) {
        var total = vendas.stream()
                .filter(venda -> venda.getCobranca().getValorCancelado().equals(BigDecimal.valueOf(0D)))
                .map(venda -> venda.getCobranca().getTaxaEntrega())
                .toList();

        return somar(total);
    }

    private BigDecimal calcularTotalDesconto(List<Venda> vendas) {
        var total = vendas.stream()
                .map(Venda::getValorDesconto)
                .toList();

        return somar(total);
    }

    private BigDecimal calcularTotalComissao(List<Venda> vendas) {
        var total = vendas.stream()
                .filter(venda -> venda.getCobranca().getValorCancelado().equals(BigDecimal.valueOf(0D)))
                .map(venda -> venda.getCobranca().getComissao())
                .toList();

        return somar(total);
    }

    private BigDecimal calcularTotalTaxas(List<Venda> vendas) {
        var total = vendas.stream()
                .map(venda -> venda.getCobranca().getTotalTaxas())
                .toList();

        return somar(total);
    }

    private BigDecimal calcularTicketMedio(List<Venda> vendas) {
        var valorBruto = calcularValorBruto(vendas);
        return valorBruto.divide(new BigDecimal(vendas.size()), RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularComissaoTransacaoPagamento(List<Venda> vendas) {
        var total = vendas.stream()
                .filter(venda -> venda.getCobranca().getValorCancelado().equals(BigDecimal.valueOf(0D)))
                .filter(venda -> venda.getCobranca().getTaxaAdquirente().compareTo(BigDecimal.ZERO) < 0)
                .map(venda -> venda.getCobranca().getTaxaAdquirente())
                .toList();

        return somar(total);
    }

    private BigDecimal getTaxaMensalidade(Empresa empresa, BigDecimal totalBruto) {
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
}

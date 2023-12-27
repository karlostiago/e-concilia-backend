package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.VendaProcessada;
import com.ctsousa.econcilia.service.VendaProcessadaService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

import static com.ctsousa.econcilia.util.CalculadoraUtil.somar;

@Component
public class VendaProcessadaImpl implements VendaProcessadaService {

    @Override
    public VendaProcessada processar(List<Venda> vendas) {

        var totalVenda = vendas.stream()
                .filter(venda -> venda.getValorCancelado().equals(BigDecimal.valueOf(0D)))
                .map(venda -> venda.getCobranca().getValorParcial())
                .toList();

        var totalTaxa = vendas.stream()
                .filter(venda -> venda.getValorCancelado().equals(BigDecimal.valueOf(0D)))
                .map(venda -> venda.getCobranca().getTaxaEntrega())
                .toList();

        var totalDesconto = vendas.stream()
                .filter(venda -> venda.getValorCancelado().equals(BigDecimal.valueOf(0D)))
                .map(venda -> venda.getCobranca().getBeneficioOperadora())
                .toList();


        var resultTotalVenda = somar(totalVenda);
        var resultTotalTaxa = somar(totalTaxa);
        var resultTotalDesconto = somar(totalDesconto);

        var resultTotalBruto = resultTotalVenda
                .add(resultTotalTaxa)
                .subtract(resultTotalDesconto);

        System.out.println(resultTotalVenda);
        System.out.println(resultTotalTaxa);
        System.out.println(resultTotalDesconto);
        System.out.println("-----------------------------");
        System.out.println(resultTotalBruto);


        return VendaProcessada
                .builder()
                .totalBruto(calcularValorBruto(vendas))
                .totalPedido(calcularTotalPedido(vendas))
                .totalCancelado(calcularTotalCancelado(vendas))
                .totalLiquido(calcularTotalLiquido(vendas))
                .totalTaxaEntrega(calcularTotalTaxaEntrega(vendas))
                .totalDesconto(calcularTotalDesconto(vendas).multiply(BigDecimal.valueOf(-1D)))
                .totalTicketMedio(calcularTicketMedio(vendas))
                .totalComissao(calcularTotalComissao(vendas))
                .quantidade(BigInteger.valueOf(vendas.size()))
                .totalTaxas(calcularTotalTaxas(vendas))
                .taxaMedia(calcularTaxaMedia(vendas))
                .totalComissaoOperadora(calcularIncentivoPromocionalOperadora(vendas))
                .totalComissaoTransacaoPagamento(calcularComissaoTransacaoPagamento(vendas))
                .totalPromocaoLoja(calcularIncentivoPromocionalLoja(vendas))
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

    private BigDecimal calcularTaxaMedia(List<Venda> vendas) {
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
}
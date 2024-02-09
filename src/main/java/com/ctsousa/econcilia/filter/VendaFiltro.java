package com.ctsousa.econcilia.filter;

import com.ctsousa.econcilia.enumaration.MetodoPagamento;
import com.ctsousa.econcilia.model.Venda;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.ctsousa.econcilia.util.StringUtil.naoTemValor;
import static com.ctsousa.econcilia.util.StringUtil.temValor;

public class VendaFiltro {

    private final List<Venda> vendas;

    private final String bandeira;

    private final String metodoPagamento;

    private final String tipoRecebimento;

    @Getter
    private List<Venda> vendasFiltradas;

    public VendaFiltro(List<Venda> vendas, String bandeira, String metodoPagamento, String tipoRecebimento) {
        this.vendas = vendas;
        this.bandeira = bandeira;
        this.metodoPagamento = metodoPagamento;
        this.tipoRecebimento = tipoRecebimento;
        this.vendasFiltradas = new ArrayList<>(vendas.size());
    }

    public VendaFiltro porBandeira() {
        if (Boolean.TRUE.equals(temValor(bandeira))) {
            vendasFiltradas.addAll(vendas.stream().filter(venda -> {
                        var descBandeira = venda.getPagamento().getBandeira().toUpperCase();
                        return descBandeira.contains(bandeira.toUpperCase());
                    })
                    .toList());
        }
        return this;
    }

    public VendaFiltro porMetodoPagamento() {
        if (Boolean.TRUE.equals(temValor(metodoPagamento))) {
            MetodoPagamento formaPagamento = MetodoPagamento.porDescricao(metodoPagamento);

            if (formaPagamento != null) {
                vendasFiltradas.addAll(vendas.stream().filter(venda -> venda.getPagamento().getMetodo().equalsIgnoreCase(formaPagamento.name()))
                        .toList());
            }
        }
        return this;
    }

    public VendaFiltro porMetodoPagamentoBandeira() {
        if (temValor(metodoPagamento) && temValor(bandeira)) {
            MetodoPagamento formaPagamento = MetodoPagamento.porDescricao(metodoPagamento);
            vendasFiltradas = vendas.stream().filter(venda -> {
                        var mPagamento = venda.getPagamento().getMetodo().toUpperCase();
                        var descBandeira = venda.getPagamento().getBandeira().toUpperCase();
                        return descBandeira.contains(bandeira.toUpperCase())
                                && mPagamento.equalsIgnoreCase(formaPagamento.name());
                    })
                    .toList();
        } else if (naoTemValor(metodoPagamento) && naoTemValor(bandeira)) {
            vendasFiltradas.addAll(vendas);
        }
        return this;
    }

    public VendaFiltro porTipoRecebimento() {
        if (Boolean.TRUE.equals(temValor(tipoRecebimento)) && "loja".equalsIgnoreCase(tipoRecebimento)) {
            vendasFiltradas = vendasFiltradas.stream()
                    .filter(venda -> "merchant".equalsIgnoreCase(venda.getPagamento().getResponsavel()))
                    .toList();
        } else if (Boolean.TRUE.equals(temValor(tipoRecebimento)) && !"loja".equalsIgnoreCase(tipoRecebimento)) {
            vendasFiltradas = vendasFiltradas.stream()
                    .filter(venda -> !"merchant".equalsIgnoreCase(venda.getPagamento().getResponsavel()))
                    .toList();
        }
        return this;
    }
}

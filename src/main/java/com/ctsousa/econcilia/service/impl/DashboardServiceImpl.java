package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.model.Cancelamento;
import com.ctsousa.econcilia.model.Cobranca;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.DashboardDTO;
import com.ctsousa.econcilia.service.DashboadService;
import com.ctsousa.econcilia.service.IntegracaoService;
import com.ctsousa.econcilia.util.StringUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DashboardServiceImpl implements DashboadService {

    private final IntegracaoService integracaoService;

    public DashboardServiceImpl(IntegracaoService integracaoService) {
        this.integracaoService = integracaoService;
    }

    @Override
    public List<Venda> buscarVendasUltimos7Dias(Long empresaId) {
        if (empresaId < 0) {
            empresaId = null;
        }

        var dtInicial = LocalDate.now().minusDays(7);
        var dtFinal = LocalDate.now();

        List<Integracao> integracoes = integracaoService.pesquisar(empresaId, null, null);

        List<Venda> vendas = new ArrayList<>();

        for (Integracao integracao : integracoes) {
            vendas.addAll(integracaoService.pesquisarVendasIfood(integracao.getCodigoIntegracao(), null, null, dtInicial, dtFinal));
        }

        return vendas;
    }

    @Override
    public DashboardDTO carregarInformacoes(Long empresaId, LocalDate dtInicial, LocalDate dtFinal) {

        if (empresaId < 0) {
            empresaId = null;
        }

        List<Integracao> integracoes = integracaoService.pesquisar(empresaId, null, null);

        List<Venda> vendas = new ArrayList<>();
        List<Cancelamento> cancelamentos = new ArrayList<>();

        for (Integracao integracao : integracoes) {
            vendas.addAll(integracaoService.pesquisarVendasIfood(integracao.getCodigoIntegracao(), null, null, dtInicial, dtFinal));
        }

        if (vendas.isEmpty()) {
            return getDashboardDTO();
        }

        return DashboardDTO.builder()
                .valorBrutoVendas(calcularValorBruto(vendas))
                .ticketMedio(calcularTicketMedio(vendas))
                .quantidadeVendas(BigInteger.valueOf(vendas.size()))
                .valorCancelamento(calcularCancelamentos(cancelamentos))
                .build();
    }

    private BigDecimal calcularValorBruto(List<Venda> vendas) {
        return vendas.stream()
                .map(venda -> venda.getCobranca().getValorBruto())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularTicketMedio(List<Venda> vendas) {
        var valorBruto = calcularValorBruto(vendas);
        return valorBruto.divide(new BigDecimal(vendas.size()), RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularCancelamentos(List<Cancelamento> cancelamentos) {
        return cancelamentos.stream()
                .map(Cancelamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private DashboardDTO getDashboardDTO() {
        return DashboardDTO
                .builder()
                .valorCancelamento(new BigDecimal("0.0"))
                .taxaMedia(new BigDecimal("0.0"))
                .valorBrutoVendas(new BigDecimal("0.0"))
                .valorReceber(new BigDecimal("0.0"))
                .valorRecebido(new BigDecimal("0.0"))
                .valorTaxas(new BigDecimal("0.0"))
                .quantidadeVendas(BigInteger.ZERO)
                .taxaMedia(new BigDecimal("0.0"))
                .build();
    }
}

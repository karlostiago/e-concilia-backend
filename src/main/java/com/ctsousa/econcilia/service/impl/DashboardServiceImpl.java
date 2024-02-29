package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.enumaration.Faixa;
import com.ctsousa.econcilia.enumaration.TipoProcessador;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.DashboardDTO;
import com.ctsousa.econcilia.model.dto.PeriodoDTO;
import com.ctsousa.econcilia.processor.Processador;
import com.ctsousa.econcilia.processor.ProcessadorFiltro;
import com.ctsousa.econcilia.service.DashboadService;
import com.ctsousa.econcilia.service.IntegracaoService;
import com.ctsousa.econcilia.util.DataUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DashboardServiceImpl implements DashboadService {

    private final IntegracaoService integracaoService;

    public DashboardServiceImpl(IntegracaoService integracaoService) {
        this.integracaoService = integracaoService;
    }

    @Override
    @Cacheable(value = "vendaAnualCache", key = "{#empresaId, #dtInicial}")
    public List<Venda> buscarVendaAnual(String empresaId, LocalDate dtInicial) {
        List<Long> empresasId = getEmpresasId(empresaId);
        List<PeriodoDTO> periodos = DataUtil.periodoAnual(dtInicial, Faixa.FX_90);
        List<Venda> vendas = new ArrayList<>();

        for (Long idEmpresa : empresasId) {
            List<Integracao> integracoes = integracaoService.pesquisar(idEmpresa, null, null);
            for (Integracao integracao : integracoes) {
                for (PeriodoDTO periodoDTO : periodos) {
                    ProcessadorFiltro processadorFiltro = getProcessadorFiltro(integracao, periodoDTO.getDe(), periodoDTO.getAte());
                    Processador processador = TipoProcessador.porOperadora(integracao.getOperadora());
                    processador.processar(processadorFiltro, false);
                    vendas.addAll(processador.getVendas());
                }
            }
        }

        return vendas;
    }

    @Override
    @Cacheable(value = "vendaMensalCache", key = "{#empresaId, #dtInicial, #dtFinal}")
    public List<Venda> buscarVendaMensal(String empresaId, LocalDate dtInicial, LocalDate dtFinal) {
        List<Long> empresasId = getEmpresasId(empresaId);

        List<Venda> vendas = new ArrayList<>();

        for (Long idEmpresa : empresasId) {
            vendas.addAll(buscarVendas(idEmpresa, dtInicial, dtFinal));
        }

        return vendas;
    }

    @Override
    public List<Venda> buscarVendasUltimos7Dias(String empresaId) {
        List<Long> empresasId = getEmpresasId(empresaId);

        var dtInicial = LocalDate.now().minusDays(7);
        var dtFinal = LocalDate.now();

        List<Venda> vendas = new ArrayList<>();

        for (Long idEmpresa : empresasId) {
            vendas.addAll(buscarVendas(idEmpresa, dtInicial, dtFinal));
        }

        return vendas;
    }

    @Override
    @Cacheable(value = "informacoesCache", key = "{#empresaId, #dtInicial, #dtFinal}")
    public DashboardDTO carregarInformacoes(String empresaId, LocalDate dtInicial, LocalDate dtFinal) {
        List<Long> empresasId = getEmpresasId(empresaId);
        DashboardDTO dashboardDTO = new DashboardDTO();
        List<Venda> vendas = new ArrayList<>();

        for (Long idEmpresa : empresasId) {
            List<Integracao> integracoes = integracaoService.pesquisar(idEmpresa, null, null);
            for (Integracao integracao : integracoes) {

                ProcessadorFiltro processadorFiltro = getProcessadorFiltro(integracao, dtInicial, dtFinal);
                Processador processador = TipoProcessador.porOperadora(integracao.getOperadora());
                processador.processar(processadorFiltro, true);

                dashboardDTO.setValorBrutoVendas(dashboardDTO.getValorBrutoVendas().add(processador.getValorTotalBruto()));
                dashboardDTO.setQuantidadeVendas(dashboardDTO.getQuantidadeVendas().add(BigInteger.valueOf(processador.getQuantidade())));
                dashboardDTO.setTicketMedio(dashboardDTO.getTicketMedio().add(processador.getValorTotalTicketMedio()));
                dashboardDTO.setValorCancelamento(dashboardDTO.getValorCancelamento().add(processador.getValorTotalCancelado()));
                dashboardDTO.setValorRecebidoLoja(dashboardDTO.getValorRecebidoLoja().add(processador.getValorTotalRecebido()));
                dashboardDTO.setValorComissaoTransacao(dashboardDTO.getValorComissaoTransacao().add(processador.getValorTotalComissaoTransacaoPagamento()));
                dashboardDTO.setValorTaxaEntrega(dashboardDTO.getValorTaxaEntrega().add(processador.getValorTotalTaxaEntrega()));
                dashboardDTO.setValorEmRepasse(dashboardDTO.getValorEmRepasse().add(processador.getValorTotalRepasse()));
                dashboardDTO.setValorComissao(dashboardDTO.getValorComissao().add(processador.getValorTotalComissao()));
                dashboardDTO.setValorPromocao(dashboardDTO.getValorPromocao().add(processador.getValorTotalPromocao()));
                vendas.addAll(processador.getVendas());
            }
        }

        dashboardDTO.setVendas(vendas);

        return dashboardDTO;
    }

    private List<Venda> buscarVendas(final Long empresaId, LocalDate dtInicial, LocalDate dtFinal) {
        List<Venda> vendas = new ArrayList<>();
        List<Integracao> integracoes = integracaoService.pesquisar(empresaId, null, null);
        for (Integracao integracao : integracoes) {
            ProcessadorFiltro processadorFiltro = getProcessadorFiltro(integracao, dtInicial, dtFinal);
            Processador processador = TipoProcessador.porOperadora(integracao.getOperadora());
            processador.processar(processadorFiltro, false);
            vendas.addAll(processador.getVendas());
        }
        return vendas;
    }

    private ProcessadorFiltro getProcessadorFiltro(Integracao integracao, LocalDate dtInicial, LocalDate dtFinal) {
        ProcessadorFiltro processadorFiltro = new ProcessadorFiltro();
        processadorFiltro.setIntegracao(integracao);
        processadorFiltro.setDtInicial(dtInicial);
        processadorFiltro.setDtFinal(dtFinal);
        return processadorFiltro;
    }

    private List<Long> getEmpresasId(final String empresasId) {
        String[] empresasIdSplit = empresasId.split(",");
        return Arrays.stream(empresasIdSplit)
                .map(s -> Long.valueOf(s.trim()))
                .toList();
    }
}

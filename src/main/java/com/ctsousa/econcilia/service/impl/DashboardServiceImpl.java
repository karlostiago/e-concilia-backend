package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.enumaration.Faixa;
import com.ctsousa.econcilia.enumaration.TipoProcessador;
import com.ctsousa.econcilia.enumaration.TipoRelatorio;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.DashboardDTO;
import com.ctsousa.econcilia.model.dto.GraficoVendaUltimo7DiaDTO;
import com.ctsousa.econcilia.model.dto.PeriodoDTO;
import com.ctsousa.econcilia.processor.Processador;
import com.ctsousa.econcilia.processor.ProcessadorFiltro;
import com.ctsousa.econcilia.report.dto.RelatorioConsolidadoDTO;
import com.ctsousa.econcilia.report.dto.RelatorioDTO;
import com.ctsousa.econcilia.repository.ConsolidadoRepository;
import com.ctsousa.econcilia.service.DashboadService;
import com.ctsousa.econcilia.service.EmpresaService;
import com.ctsousa.econcilia.service.IntegracaoService;
import com.ctsousa.econcilia.util.DataUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ctsousa.econcilia.util.DecimalUtil.paraDecimal;
import static com.ctsousa.econcilia.util.StringUtil.naoTemValor;

@Component
public class DashboardServiceImpl implements DashboadService {

    private final IntegracaoService integracaoService;

    private final ConsolidadoRepository consolidadoRepository;

    private final EmpresaService empresaService;

    public DashboardServiceImpl(IntegracaoService integracaoService, ConsolidadoRepository consolidadoRepository, EmpresaService empresaService) {
        this.integracaoService = integracaoService;
        this.consolidadoRepository = consolidadoRepository;
        this.empresaService = empresaService;
    }

    @Override
    public DashboardDTO carregaVendasConsolidadas(String empresaId, LocalDate dtInicial, LocalDate dtFinal) {
        List<Long> empresasId = getEmpresasId(empresaId);
        DashboardDTO dashboardDTO = new DashboardDTO();

        TipoRelatorio tipoRelatorio = TipoRelatorio.CONSOLIDACAO;

        BigDecimal totalValorBrutoVendas = BigDecimal.valueOf(0D);
        BigInteger totalQuantidadeVendas = BigInteger.ZERO;
        BigDecimal totalTicketMedio = BigDecimal.valueOf(0D);
        BigDecimal totalValorCancelamento = BigDecimal.valueOf(0D);
        BigDecimal totalValorRecebidoLoja = BigDecimal.valueOf(0D);
        BigDecimal totalValorTaxaEntrega = BigDecimal.valueOf(0D);
        BigDecimal totalValorComissao = BigDecimal.valueOf(0D);
        BigDecimal totalValorPromocao = BigDecimal.valueOf(0D);
        BigDecimal totalValorComissaoTransacao = BigDecimal.valueOf(0D);
        BigDecimal totalValorEmRepasse = BigDecimal.valueOf(0D);

        RelatorioDTO relatorioDTO = null;

        for (Long idEmpresa : empresasId) {
            Empresa empresa = empresaService.pesquisarPorId(idEmpresa);

            try {
                relatorioDTO = tipoRelatorio.gerarDados(consolidadoRepository, dtInicial, dtFinal, empresa, new Operadora());
            } catch (NotificacaoException e) {
                continue;
            }

            RelatorioConsolidadoDTO consolidadoDTO = relatorioDTO.getConsolidados().stream()
                            .filter(relatorioConsolidadoDTO -> naoTemValor(relatorioConsolidadoDTO.getPeriodo()))
                            .findFirst().orElse(null);

            if (consolidadoDTO != null) {
                totalValorBrutoVendas = totalValorBrutoVendas.add(paraDecimal(consolidadoDTO.getTotalBruto()));
                totalQuantidadeVendas = totalQuantidadeVendas.add(paraDecimal(consolidadoDTO.getQuantidadeVenda()).toBigInteger());
                totalTicketMedio = totalTicketMedio.add(paraDecimal(consolidadoDTO.getTicketMedio()));
                totalValorCancelamento = totalValorCancelamento.add(paraDecimal(consolidadoDTO.getTotalCancelado()));
                totalValorRecebidoLoja = totalValorRecebidoLoja.add(paraDecimal(consolidadoDTO.getValorAntecipado()));
                totalValorTaxaEntrega = totalValorTaxaEntrega.add(paraDecimal(consolidadoDTO.getTotalTaxaEntrega()));
                totalValorComissao = totalValorComissao.add(paraDecimal(consolidadoDTO.getTotalComissao()));
                totalValorPromocao = totalValorPromocao.add(paraDecimal(consolidadoDTO.getTotalPromocao()));
                totalValorComissaoTransacao = totalValorComissaoTransacao.add(paraDecimal(consolidadoDTO.getTotalTransacaoPagamento()));
                totalValorEmRepasse = totalValorEmRepasse.add(paraDecimal(consolidadoDTO.getTotalRepasse()));
            }
        }

        dashboardDTO.setValorBrutoVendas(totalValorBrutoVendas);
        dashboardDTO.setQuantidadeVendas(totalQuantidadeVendas);
        dashboardDTO.setTicketMedio(totalTicketMedio);
        dashboardDTO.setValorCancelamento(totalValorCancelamento);
        dashboardDTO.setValorRecebidoLoja(totalValorRecebidoLoja);
        dashboardDTO.setValorTaxaEntrega(totalValorTaxaEntrega);
        dashboardDTO.setValorComissao(totalValorComissao);
        dashboardDTO.setValorPromocao(totalValorPromocao);
        dashboardDTO.setValorComissaoTransacao(totalValorComissaoTransacao);
        dashboardDTO.setValorEmRepasse(totalValorEmRepasse);

        carregarDadosParaAlimetarGraficos(relatorioDTO, dashboardDTO, dtFinal);

        return dashboardDTO;
    }

    private void carregarDadosParaAlimetarGraficos(RelatorioDTO relatorioDTO, DashboardDTO dashboardDTO, LocalDate periodo) {
        if (relatorioDTO != null) {
            dashboardDTO.setGraficoVendaUltimo7DiaDTO(new GraficoVendaUltimo7DiaServiceImpl().processar(periodo, relatorioDTO.getConsolidados()));
        }
    }

    @Override
    @Cacheable(value = "informacoesCache", key = "{#empresaId, #dtInicial, #dtFinal}")
    public DashboardDTO carregarInformacoes(String empresaId, LocalDate dtInicial, LocalDate dtFinal) {
        List<Long> empresasId = getEmpresasId(empresaId);
        DashboardDTO dashboardDTO = new DashboardDTO();

        for (Long idEmpresa : empresasId) {
            List<Integracao> integracoes = integracaoService.pesquisar(idEmpresa, null, null);
            for (Integracao integracao : integracoes) {

                ProcessadorFiltro processadorFiltro = new ProcessadorFiltro(integracao, dtInicial, dtFinal);
                Processador processador = TipoProcessador.porOperadora(integracao.getOperadora());
                processador.processar(processadorFiltro, true, false);

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
            }
        }

        dashboardDTO.setVendas(buscarVendaAnual(empresaId, dtFinal));
        return dashboardDTO;
    }

    @Cacheable(value = "vendaAnualCache", key = "{#empresaId, #dtInicial}")
    private List<Venda> buscarVendaAnual(String empresaId, LocalDate dtInicial) {
        List<Long> empresasId = getEmpresasId(empresaId);
        List<PeriodoDTO> periodos = DataUtil.periodoAnual(dtInicial, Faixa.FX_60);
        List<Venda> vendas = new ArrayList<>();

        for (Long idEmpresa : empresasId) {
            List<Integracao> integracoes = integracaoService.pesquisar(idEmpresa, null, null);
            for (Integracao integracao : integracoes) {
                for (PeriodoDTO periodoDTO : periodos) {
                    ProcessadorFiltro processadorFiltro = new ProcessadorFiltro(integracao, periodoDTO.getDe(), periodoDTO.getAte());
                    Processador processador = TipoProcessador.porOperadora(integracao.getOperadora());
                    processador.processar(processadorFiltro, false, false);
                    vendas.addAll(processador.getVendas());
                }
            }
        }

        return vendas;
    }

    private List<Long> getEmpresasId(final String empresasId) {
        String[] empresasIdSplit = empresasId.split(",");
        return Arrays.stream(empresasIdSplit)
                .map(s -> Long.valueOf(s.trim()))
                .toList();
    }
}

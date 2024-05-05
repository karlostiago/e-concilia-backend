package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.enumaration.TipoRelatorio;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.graphic.GraficoPercentualVendaFormaPagamento;
import com.ctsousa.econcilia.graphic.GraficoVendaAnual;
import com.ctsousa.econcilia.graphic.GraficoVendaMensal;
import com.ctsousa.econcilia.graphic.GraficoVendaSemanalAcumulada;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.DashboardDTO;
import com.ctsousa.econcilia.model.dto.GraficoDTO;
import com.ctsousa.econcilia.report.dto.RelatorioConsolidadoDTO;
import com.ctsousa.econcilia.report.dto.RelatorioDTO;
import com.ctsousa.econcilia.repository.ConsolidadoRepository;
import com.ctsousa.econcilia.repository.VendaRepository;
import com.ctsousa.econcilia.service.DashboadService;
import com.ctsousa.econcilia.service.EmpresaService;
import com.ctsousa.econcilia.service.IntegracaoService;
import com.ctsousa.econcilia.util.DataUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

import static com.ctsousa.econcilia.util.DataUtil.*;
import static com.ctsousa.econcilia.util.DecimalUtil.paraDecimal;
import static com.ctsousa.econcilia.util.StringUtil.naoTemValor;
import static com.ctsousa.econcilia.util.StringUtil.temValor;
import static java.time.YearMonth.from;

@Component
public class DashboardServiceImpl implements DashboadService {

    private final ConsolidadoRepository consolidadoRepository;

    private final EmpresaService empresaService;

    private final VendaRepository vendaRepository;

    private Map<String, Map<YearMonth, BigDecimal>> vendasAnuaisMap;

    private Map<String, Map<LocalDate, BigDecimal>> vendasMensalMap;

    private Map<String, Map<LocalDate, BigDecimal>> vendasSemanalAcumuladaMap;

    public DashboardServiceImpl(ConsolidadoRepository consolidadoRepository, EmpresaService empresaService, VendaRepository vendaRepository) {
        this.consolidadoRepository = consolidadoRepository;
        this.empresaService = empresaService;
        this.vendaRepository = vendaRepository;
    }

    @Override
    public DashboardDTO carregaVendasConsolidadas(String empresaId, LocalDate dtInicial, LocalDate dtFinal) {
        List<Venda> vendas = new ArrayList<>();
        vendasAnuaisMap = new HashMap<>();
        vendasMensalMap = new HashMap<>();
        vendasSemanalAcumuladaMap = new HashMap<>();

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

        RelatorioDTO relatorioDTO;

        for (Long idEmpresa : empresasId) {
            Empresa empresa = empresaService.pesquisarPorId(idEmpresa);

            if (empresa == null) continue;

            if (vendas.isEmpty()) {
                vendas = vendaRepository.buscarPor(empresa, dtInicial, dtFinal);
            }

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

                carregarDadosAlimentarGraficos(empresa, tipoRelatorio, dtFinal);
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

        GraficoDTO graficoDTO = GraficoDTO.builder()
                .graficoVendaMensalDTO(new GraficoVendaMensal().construir(vendasMensalMap))
                .graficoVendaAnualDTO(new GraficoVendaAnual().construir(vendasAnuaisMap))
                .graficoVendaUltimo7DiaDTO(new GraficoVendaSemanalAcumulada().construir(vendasSemanalAcumuladaMap))
                .graficoPercentualVendaFormaPagamentoDTO(new GraficoPercentualVendaFormaPagamento().construir(vendas))
                .build();

        dashboardDTO.setGraficoDTO(graficoDTO);

        return dashboardDTO;
    }

    private void carregarDadosAlimentarGraficos(final Empresa empresa, final TipoRelatorio tipoRelatorio, final LocalDate dtFinal) {
        try {
            LocalDate dtInicial = dtFinal.minusYears(1).withDayOfMonth(1);
            RelatorioDTO relatorioDTO = tipoRelatorio.gerarDados(consolidadoRepository, dtInicial, dtFinal, empresa, new Operadora());
            popularDadoGraficoSemanal(relatorioDTO, dtFinal);
            popularDadoGraficoMensal(relatorioDTO, dtFinal);
            popularDadoGraficoAnual(relatorioDTO);
        } catch (NotificacaoException e) {
            //
        }
    }

    private void popularDadoGraficoMensal(final RelatorioDTO relatorioDTO, final LocalDate dtFinal) {
        LocalDate dtInicial = dtFinal.withDayOfMonth(1);

        List<RelatorioConsolidadoDTO> consolidados = relatorioDTO.getConsolidados().stream()
                .filter(c -> temValor(c.getPeriodo()))
                .filter(c -> paraLocalDate(c.getPeriodo()).isAfter(dtInicial) && paraLocalDate(c.getPeriodo()).isBefore(dtFinal))
                .toList();

        String nomeEmpresa = consolidados.get(0).getInfo().getNome();
        Map<LocalDate, BigDecimal> mapConsolidados = new TreeMap<>();

        for (RelatorioConsolidadoDTO consolidadoDTO : consolidados) {
            LocalDate periodo = DataUtil.paraLocalDate(consolidadoDTO.getPeriodo());
            mapConsolidados.put(periodo, paraDecimal(consolidadoDTO.getTotalBruto()));
        }

        vendasMensalMap.put(nomeEmpresa,  mapConsolidados);
    }

    private void popularDadoGraficoSemanal(final RelatorioDTO relatorioDTO, final LocalDate dtFinal) {
        LocalDate dataInicial = dtFinal.minusWeeks(1);
        LocalDate dataFinal = dataInicial.plusWeeks(1);

        if (isMesCorrente(dtFinal)) {
            dataInicial = LocalDate.now().minusDays(1).minusWeeks(1);
            dataFinal = dataInicial.plusWeeks(1);
        }

        LocalDate finalDataInicial = dataInicial;
        LocalDate finalDataFinal = dataFinal;

        List<RelatorioConsolidadoDTO> consolidados = relatorioDTO.getConsolidados().stream()
                .filter(c -> temValor(c.getPeriodo()))
                .filter(c -> paraLocalDate(c.getPeriodo()).isAfter(finalDataInicial) && paraLocalDate(c.getPeriodo()).isBefore(finalDataFinal))
                .toList();

        String nomeEmpresa = consolidados.get(0).getInfo().getNome();
        Map<LocalDate, BigDecimal> mapConsolidados = new TreeMap<>();

        for (RelatorioConsolidadoDTO consolidadoDTO : consolidados) {
            LocalDate periodo = paraLocalDate(consolidadoDTO.getPeriodo());
            BigDecimal totalBruto = mapConsolidados.getOrDefault(periodo, BigDecimal.valueOf(0D));
            totalBruto = totalBruto.add(paraDecimal(consolidadoDTO.getTotalBruto()));
            mapConsolidados.put(periodo, totalBruto);
        }

        vendasSemanalAcumuladaMap.put(nomeEmpresa, mapConsolidados);
    }

    private void popularDadoGraficoAnual(final RelatorioDTO relatorioDTO) {
        List<RelatorioConsolidadoDTO> consolidados = relatorioDTO.getConsolidados().stream()
                .filter(c -> temValor(c.getPeriodo()))
                .toList();

        String nomeEmpresa = consolidados.get(0).getInfo().getNome();
        Map<YearMonth, BigDecimal> mapConsolidados = new TreeMap<>();

        for (RelatorioConsolidadoDTO consolidadoDTO : consolidados) {
            LocalDate periodo = parseMesAno(consolidadoDTO.getPeriodo(), "yyyy-MM-dd");
            BigDecimal totalBruto = mapConsolidados.getOrDefault(from(periodo), BigDecimal.valueOf(0D));
            totalBruto = totalBruto.add(paraDecimal(consolidadoDTO.getTotalBruto()));
            mapConsolidados.put(from(periodo), totalBruto);
        }

        vendasAnuaisMap.put(nomeEmpresa, mapConsolidados);
    }

    private List<Long> getEmpresasId(final String empresasId) {
        String[] empresasIdSplit = empresasId.split(",");
        return Arrays.stream(empresasIdSplit)
                .map(s -> Long.valueOf(s.trim()))
                .toList();
    }
}

package com.ctsousa.econcilia.processor.ifood;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.*;
import com.ctsousa.econcilia.processor.Processador;
import com.ctsousa.econcilia.processor.ProcessadorFiltro;
import com.ctsousa.econcilia.service.*;
import com.ctsousa.econcilia.util.DecimalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ctsousa.econcilia.util.CalculadoraUtil.somar;
import static com.ctsousa.econcilia.util.DataUtil.getPrimeiroDiaMes;
import static com.ctsousa.econcilia.util.DataUtil.getUltimoDiaMes;

@Slf4j
@Component
public class ProcessadorIfood extends Processador {

    private final TaxaService taxaService;

    private final VendaService vendaService;

    private final OcorrenciaService ocorrenciaService;

    private final CancelamentoService cancelamentoService;

    private final AjusteVendaService ajusteVendaService;

    private final ConsolidacaoService consolidacaoService;

    public ProcessadorIfood(TaxaService taxaService,
                            VendaService vendaService,
                            OcorrenciaService ocorrenciaService,
                            CancelamentoService cancelamentoService,
                            AjusteVendaService ajusteVendaService, ConsolidacaoService consolidacaoService) {
        this.taxaService = taxaService;
        this.vendaService = vendaService;
        this.ocorrenciaService = ocorrenciaService;
        this.cancelamentoService = cancelamentoService;
        this.ajusteVendaService = ajusteVendaService;
        this.consolidacaoService = consolidacaoService;
    }

    @Override
    public void processar(ProcessadorFiltro processadorFiltro, boolean executarCalculo, boolean consolidar) {
        log.info(" ::: Iniciando processador IFOOD ::: ");

        carregarVendas(processadorFiltro);

        if (vendas.isEmpty()) {
            log.info(" ::: Finalizado processador IFOOD, nenhuma venda foi encontrada ::: ");
            reiniciar();
            return;
        }

        if (executarCalculo) {
            log.info(" ::: Buscando ocorrencias ::: ");
            var ocorrencias = ocorrenciaService.buscar(processadorFiltro.getIntegracao(), processadorFiltro.getDtInicial(), processadorFiltro.getDtFinal());

            log.info(" ::: Processando cancelamento se houver ::: ");
            processarCancelamento(processadorFiltro.getIntegracao().getCodigoIntegracao());

            log.info(" ::: Reprocessando vendas ::: ");
            reprocessar(processadorFiltro.getDtInicial(), processadorFiltro.getDtFinal(), processadorFiltro.getIntegracao().getCodigoIntegracao());

            log.info(" ::: Calculo das vendas iniciado ::: ");
            calcular(ocorrencias, consolidar);
        }

        log.info(" ::: Finalizado processador IFOOD ::: ");
    }

    private void carregarVendas(ProcessadorFiltro processadorFiltro) {
        var formaRecebimento = processadorFiltro.getFormaRecebimento() != null ? processadorFiltro.getFormaRecebimento().getDescricao() : null;

        log.info(" ::: Buscando vendas ::: ");
        vendas = vendaService.buscar(processadorFiltro.getIntegracao().getEmpresa(),
                processadorFiltro.getIntegracao().getOperadora(),
                processadorFiltro.getDtInicial(),
                processadorFiltro.getDtFinal(),
                processadorFiltro.getFormaPagamento(),
                processadorFiltro.getCartaoBandeira(),
                formaRecebimento);
    }

    private void processarCancelamento(String codigoLoja) {
        Map<String, Venda> vendaMap = vendas.stream().collect(Collectors.toMap(
            Venda::getPedidoId, venda -> venda, (vendaAtual, vendaNova) -> vendaAtual
        ));

        Map<String, List<Venda>> periodIdMap = vendas.stream().collect(Collectors.groupingBy(
            venda -> (venda.getPeriodoId() != null) ? venda.getPeriodoId() : "",
            Collectors.toList()
        ));

        List<String> periodoIds = periodIdMap.keySet().stream().toList();

        for (String periodId : periodoIds) {
            if (periodId.isEmpty()) continue;

            List<Cancelamento> cancelamentos = cancelamentoService.buscar(codigoLoja, periodId);

            if (!cancelamentos.isEmpty()) {
                log.info("Total de cancelamentos encontrados ::: {}", cancelamentos.size());
                for (Cancelamento cancelamento : cancelamentos) {
                    Venda venda = vendaMap.get(cancelamento.getPedidoId());
                    if (venda != null) {
                        venda.getCobranca().setValorCancelado(cancelamento.getValor());
                    }
                }
            }
        }
    }

    private void reprocessar(LocalDate dtInicial, LocalDate dtFinal, String lojaId) {
        List<AjusteVenda> ajusteVendas = ajusteVendaService.buscar(lojaId, dtInicial, dtFinal);
        log.info("Total de vendas a serem reprocessadas ::: {}", ajusteVendas.size());

        if (ajusteVendas.isEmpty()) return;

        Map<String, Venda> vendaMap = vendas.stream().collect(Collectors.toMap(
                Venda::getPedidoId, venda -> venda, (vendaAtual, vendaNova) -> vendaAtual
        ));

        for (AjusteVenda ajuste : ajusteVendas) {
            Venda venda = vendaMap.get(ajuste.getPedidoId());
            if (venda != null) {
                venda.setCobranca(ajuste.getCobranca());
                venda.setReprocessada(Boolean.TRUE);
            }
        }
    }

    private void calcular(List<Ocorrencia> ocorrencias, boolean consolidar) {
        var empresa = vendas.get(0).getEmpresa();
        var operadora = vendas.get(0).getOperadora();
        var periodo = vendas.get(0).getDataPedido();

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
        var valorTotalPedido = BigDecimal.valueOf(0D);
        var valorTotalLiquido = BigDecimal.valueOf(0D);

        for (Venda venda : vendas) {
            if (venda.getCobranca().getValorCancelado().compareTo(BigDecimal.valueOf(0)) != 0) {
                valorTotalCancelado = valorTotalCancelado.add(venda.getValorCancelado());
                continue;
            }

            valorTotalReembolsoBeneficio = valorTotalReembolsoBeneficio.add(getValorReembolsoBeneficio(venda.getCobranca()));
            valorTotalPedidoMinimo = valorTotalPedidoMinimo.add(getValorPedidoMinimo(venda.getCobranca()));
            valorTotalPromocao = valorTotalPromocao.add(venda.getCobranca().getBeneficioComercio().abs());
            valorTotalComissao = calcularValorComissao(venda.getCobranca(), valorTotalComissao);
            valorTotalRecebidoViaLoja = valorTotalRecebidoViaLoja.add(getValorRecebido(venda, operadora));
            valorTotalTransacaoPagamento = valorTotalTransacaoPagamento.add(venda.getCobranca().getTaxaAdquirente());
            valorTotalRepasse = valorTotalRepasse.add(venda.getCobranca().getTotalCredito().subtract(venda.getCobranca().getTotalDebito()));
            valorTotalBrutoParcial = valorTotalBrutoParcial.add(venda.getValorBruto());
            valorTotalTaxaEntrega = valorTotalTaxaEntrega.add(venda.getCobranca().getTaxaEntrega());
            valorTotalPedido = valorTotalPedido.add(venda.getValorTotalPedido());
            valorTotalLiquido = valorTotalLiquido.add(venda.getValorLiquido());
        }

        boolean temTaxaManutencao = consolidacaoService.temMensalidade(empresa, operadora, getPrimeiroDiaMes(periodo), getUltimoDiaMes(periodo));

        var valorBruto = consolidacaoService.buscarValorBruto(empresa, operadora, getPrimeiroDiaMes(periodo), getUltimoDiaMes(periodo));
        var valorTotalMensalidade = temTaxaManutencao ? BigDecimal.valueOf(0D) : calcularTaxaMensalidade(empresa, valorBruto);

        var valorTotalOcorrencia = somar(ocorrencias.stream()
                .map(Ocorrencia::getValor)
                .toList());

        var valorOutrosLancamentos = calcularOutrosLancamentos(vendas, ocorrencias, valorTotalMensalidade);

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

        this.valorTotalBruto = valorTotalBruto;
        this.valorTotalTicketMedio = calcularTicketMedio(BigInteger.valueOf(vendas.size()), valorTotalBruto);
        this.valorTotalCancelado = valorTotalCancelado;
        this.valorTotalRecebido = valorTotalRecebidoViaLoja;
        this.valorTotalComissaoTransacaoPagamento = valorTotalTransacaoPagamento;
        this.valorTotalComissao = valorTotalComissao;
        this.valorTotalTaxaEntrega = valorTotalTaxaEntrega;
        this.valorTotalRepasse = valorTotalRepasse;
        this.valorTotalPromocao = valorTotalPromocao.multiply(BigDecimal.valueOf(-1D));
        this.valorTotalPedido = valorTotalPedido;
        this.valorTotalLiquido = valorTotalLiquido;
        this.quantidade = vendas.size();
        this.valorTotalTaxaService = valorTotalPedidoMinimo;
        this.valorTotalManutencao = valorTotalMensalidade;
        this.empresa = empresa;
        this.operadora = operadora;
        this.periodo = periodo;

        if (consolidar) {
            log.info(" ::: Consolidando vendas ::: ");
            consolidacaoService.consolidar(this);
        }
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

    private BigDecimal calcularOutrosLancamentos(List<Venda> vendas, List<Ocorrencia> ocorrencias, BigDecimal taxaMensalidade) {
        var valorReembolso = calcularTotalReembolso(vendas);

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

    private BigDecimal calcularTotalReembolso(List<Venda> vendas) {
        var vendasComTaxaServico = vendas.stream()
                .filter(venda -> DecimalUtil.iqualZero(venda.getValorCancelado()))
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

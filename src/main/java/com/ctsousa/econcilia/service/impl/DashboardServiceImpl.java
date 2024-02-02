package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.enumaration.TipoProcessador;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Ocorrencia;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.VendaProcessada;
import com.ctsousa.econcilia.model.dto.DashboardDTO;
import com.ctsousa.econcilia.processador.Processador;
import com.ctsousa.econcilia.processador.ProcessadorFactory;
import com.ctsousa.econcilia.service.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DashboardServiceImpl implements DashboadService {

    private final IntegracaoService integracaoService;

    private final IntegracaoIfoodService integracaoIfoodService;

    private final VendaProcessadaService vendaProcessadaService;

    private final ConciliadorIfoodService conciliadorIfoodService;

    private final ProcessadorFactory processadorFactory;

    public DashboardServiceImpl(IntegracaoService integracaoService, VendaProcessadaService vendaProcessadaService, ConciliadorIfoodService conciliadorIfoodService, IntegracaoIfoodService integracaoIfoodService, ProcessadorFactory processadorFactory) {
        this.integracaoService = integracaoService;
        this.vendaProcessadaService = vendaProcessadaService;
        this.conciliadorIfoodService = conciliadorIfoodService;
        this.integracaoIfoodService = integracaoIfoodService;
        this.processadorFactory = processadorFactory;
    }

    @Override
    public List<Venda> buscarVendasUltimos7Dias(String empresaId) {
        List<Long> empresasId = getEmpresasId(empresaId);

        var dtInicial = LocalDate.now().minusDays(7);
        var dtFinal = LocalDate.now();

        List<Venda> vendas = new ArrayList<>();

        for (Long idEmpresa : empresasId) {
            List<Integracao> integracoes = integracaoService.pesquisar(idEmpresa, null, null);
            for (Integracao integracao : integracoes) {
                vendas.addAll(integracaoIfoodService.pesquisarVendas(integracao.getCodigoIntegracao(), null, null, null, dtInicial, dtFinal));
            }
        }

        return vendas;
    }

    @Override
    public DashboardDTO carregarInformacoes(String empresaId, LocalDate dtInicial, LocalDate dtFinal) {
        List<Long> empresasId = getEmpresasId(empresaId);

        List<Venda> vendas = new ArrayList<>();
        List<Ocorrencia> ocorrencias = new ArrayList<>();

        for (Long idEmpresa : empresasId) {
            List<Integracao> integracoes = integracaoService.pesquisar(idEmpresa, null, null);
            for (Integracao integracao : integracoes) {

                Processador<?> processador = TipoProcessador.porOperadora(integracao.getOperadora(), processadorFactory);
                processador.processar(new ArrayList<>());

                var vendasPesquidas = integracaoIfoodService.pesquisarVendas(integracao.getCodigoIntegracao(), null, null, null, dtInicial, dtFinal);
                conciliadorIfoodService.aplicarCancelamento(vendasPesquidas, integracao.getCodigoIntegracao());
                var ocorrenciasPesquisadas = integracaoIfoodService.pesquisarOcorrencias(integracao.getCodigoIntegracao(), dtInicial, dtFinal);
                conciliadorIfoodService.reprocessarVenda(dtInicial, dtFinal, integracao.getCodigoIntegracao(), vendasPesquidas);

                vendas.addAll(vendasPesquidas);
                ocorrencias.addAll(ocorrenciasPesquisadas);
            }
        }

        if (vendas.isEmpty()) {
            return DashboardDTO.builder().build();
        }

        VendaProcessada vendaProcessada = vendaProcessadaService.processar(vendas, ocorrencias);

        return DashboardDTO.builder()
                .valorBrutoVendas(vendaProcessada.getTotalBruto())
                .ticketMedio(vendaProcessada.getTotalTicketMedio())
                .quantidadeVendas(vendaProcessada.getQuantidade())
                .valorCancelamento(vendaProcessada.getTotalCancelado())
                .valorRecebidoLoja(vendaProcessada.getTotalRecebidoLoja().multiply(BigDecimal.valueOf(-1D)))
                .valorComissaoTransacao(vendaProcessada.getTotalComissaoTransacaoPagamento())
                .valorTaxaEntrega(vendaProcessada.getTotalTaxaEntrega())
                .valorEmRepasse(vendaProcessada.getTotalRepasse())
                .valorComissao(vendaProcessada.getTotalComissao())
                .valorPromocao(vendaProcessada.getTotalPromocao())
                .build();
    }

    private List<Long> getEmpresasId(final String empresasId) {
        String[] empresasIdSplit = empresasId.split(",");
        return Arrays.stream(empresasIdSplit)
                .map(s -> Long.valueOf(s.trim()))
                .toList();
    }
}

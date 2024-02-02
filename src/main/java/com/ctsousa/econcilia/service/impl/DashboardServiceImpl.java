package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.enumaration.TipoProcessador;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.DashboardDTO;
import com.ctsousa.econcilia.processador.Processador;
import com.ctsousa.econcilia.service.DashboadService;
import com.ctsousa.econcilia.service.IntegracaoIfoodService;
import com.ctsousa.econcilia.service.IntegracaoService;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DashboardServiceImpl implements DashboadService {

    private final IntegracaoService integracaoService;

    private final IntegracaoIfoodService integracaoIfoodService;


    public DashboardServiceImpl(IntegracaoService integracaoService, IntegracaoIfoodService integracaoIfoodService) {
        this.integracaoService = integracaoService;
        this.integracaoIfoodService = integracaoIfoodService;
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
        DashboardDTO dashboardDTO = new DashboardDTO();

        for (Long idEmpresa : empresasId) {
            List<Integracao> integracoes = integracaoService.pesquisar(idEmpresa, null, null);
            for (Integracao integracao : integracoes) {

                Processador processador = TipoProcessador.porOperadora(integracao.getOperadora());
                processador.processar(integracao, dtInicial, dtFinal);

                dashboardDTO.setValorBrutoVendas(dashboardDTO.getValorBrutoVendas().add(processador.getValorTotalBruto()));
                dashboardDTO.setQuantidadeVendas(dashboardDTO.getQuantidadeVendas().add(BigInteger.valueOf(processador.getQuantidade())));
                dashboardDTO.setTicketMedio(dashboardDTO.getTicketMedio().add(processador.getValorTotalTicketMedio()));
                dashboardDTO.setValorCancelamento(dashboardDTO.getValorCancelamento().add(processador.getValorTotalCancelado()));
                dashboardDTO.setValorRecebidoLoja(dashboardDTO.getValorRecebidoLoja().add(processador.getValorTotalRecebido()));
                dashboardDTO.setValorComissaoTransacao(dashboardDTO.getValorComissaoTransacao().add(processador.getValorTotalComissaoTransacaoPagamento()));
                dashboardDTO.setValorTaxaEntrega(dashboardDTO.getValorTaxaEntrega().add(processador.getValorTotalTaxaEntrega()));
                dashboardDTO.setValorEmRepasse(dashboardDTO.getValorEmRepasse().add(processador.getValorTotalPagar()));
                dashboardDTO.setValorComissao(dashboardDTO.getValorComissao().add(processador.getValorTotalComissao()));
                dashboardDTO.setValorPromocao(dashboardDTO.getValorPromocao().add(processador.getValorTotalPromocao()));
            }
        }

        return dashboardDTO;
    }

    private List<Long> getEmpresasId(final String empresasId) {
        String[] empresasIdSplit = empresasId.split(",");
        return Arrays.stream(empresasIdSplit)
                .map(s -> Long.valueOf(s.trim()))
                .toList();
    }
}

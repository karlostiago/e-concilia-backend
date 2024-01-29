package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Ocorrencia;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.VendaProcessada;
import com.ctsousa.econcilia.model.dto.DashboardDTO;
import com.ctsousa.econcilia.service.ConciliadorIfoodService;
import com.ctsousa.econcilia.service.DashboadService;
import com.ctsousa.econcilia.service.IntegracaoService;
import com.ctsousa.econcilia.service.VendaProcessadaService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DashboardServiceImpl implements DashboadService {

    private final IntegracaoService integracaoService;

    private final VendaProcessadaService vendaProcessadaService;

    private final ConciliadorIfoodService conciliadorIfoodService;

    public DashboardServiceImpl(IntegracaoService integracaoService, VendaProcessadaService vendaProcessadaService, ConciliadorIfoodService conciliadorIfoodService) {
        this.integracaoService = integracaoService;
        this.vendaProcessadaService = vendaProcessadaService;
        this.conciliadorIfoodService = conciliadorIfoodService;
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
            vendas.addAll(integracaoService.pesquisarVendasIfood(integracao.getCodigoIntegracao(), null, null, null, dtInicial, dtFinal));
        }

        return vendas;
    }

    @Override
    public DashboardDTO carregarInformacoes(String empresaId, LocalDate dtInicial, LocalDate dtFinal) {
        String[] empresasIdSplit = empresaId.split(",");

        List<Long> empresasId = Arrays.stream(empresasIdSplit)
                .map(s -> Long.valueOf(s.trim()))
                .toList();

        List<Venda> vendas = new ArrayList<>();
        List<Ocorrencia> ocorrencias = new ArrayList<>();

        for (Long idEmpresa : empresasId) {
            List<Integracao> integracoes = integracaoService.pesquisar(idEmpresa, null, null);
            for (Integracao integracao : integracoes) {
                var vendasPesquidas = integracaoService.pesquisarVendasIfood(integracao.getCodigoIntegracao(), null, null, null, dtInicial, dtFinal);
                conciliadorIfoodService.aplicarCancelamento(vendasPesquidas, integracao.getCodigoIntegracao());
                ocorrencias = integracaoService.pesquisarOcorrencias(integracao.getCodigoIntegracao(), dtInicial, dtFinal);
                conciliadorIfoodService.reprocessarVenda(dtInicial, dtFinal, integracao.getCodigoIntegracao(), vendasPesquidas);
                vendas.addAll(vendasPesquidas);
            }
        }

        if (vendas.isEmpty()) {
            return getDashboardDTO();
        }

        VendaProcessada vendaProcessada = vendaProcessadaService.processar(vendas, ocorrencias);

        return DashboardDTO.builder()
                .valorBrutoVendas(vendaProcessada.getTotalBruto())
                .ticketMedio(vendaProcessada.getTotalTicketMedio())
                .quantidadeVendas(vendaProcessada.getQuantidade())
                .valorCancelamento(vendaProcessada.getTotalCancelado())
                .valorRecebidoLoja(vendaProcessada.getTotalRecebidoLoja().multiply(BigDecimal.valueOf(-1D)))
                .valorIncentivoPromocionalLoja(vendaProcessada.getTotalPromocaoLoja().multiply(BigDecimal.valueOf(-1D)))
                .valorIncentivoPromocionalOperadora(vendaProcessada.getTotalComissaoOperadora())
                .valorTaxaEntrega(vendaProcessada.getTotalTaxaEntrega())
                .valorEmRepasse(vendaProcessada.getTotalRepasse().subtract(BigDecimal.valueOf(100)))
//                .valorComissao(vendaProcessada.getTotalComissao())
//                .valorDesconto(vendaProcessada.getTotalDesconto())
//                .valorTaxas(vendaProcessada.getTotalTaxas())
//                .taxaMedia(vendaProcessada.getTaxaMedia())
                .build();
    }

    private DashboardDTO getDashboardDTO() {
        return DashboardDTO
                .builder()
                .valorCancelamento(new BigDecimal("0.0"))
                .taxaMedia(new BigDecimal("0.0"))
                .valorBrutoVendas(new BigDecimal("0.0"))
                .valorComissao(new BigDecimal("0.0"))
                .valorDesconto(new BigDecimal("0.0"))
                .valorTaxas(new BigDecimal("0.0"))
                .quantidadeVendas(BigInteger.ZERO)
                .taxaMedia(new BigDecimal("0.0"))
                .build();
    }
}

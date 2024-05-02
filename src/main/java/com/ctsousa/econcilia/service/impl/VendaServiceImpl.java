package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.enumaration.TipoRelatorio;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.filter.VendaFilter;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.report.dto.RelatorioDTO;
import com.ctsousa.econcilia.report.dto.RelatorioVendaDTO;
import com.ctsousa.econcilia.report.Relatorio;
import com.ctsousa.econcilia.repository.VendaRepository;
import com.ctsousa.econcilia.service.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class VendaServiceImpl implements VendaService {

    private final IntegracaoIfoodService integracaoIfoodService;

    private final VendaRepository vendaRepository;

    private final IntegracaoService integracaoService;

    private final Relatorio relatorioService;

    private final EmpresaService empresaService;

    public VendaServiceImpl(IntegracaoIfoodService integracaoIfoodService, VendaRepository vendaRepository, IntegracaoService integracaoService, Relatorio relatorioService, EmpresaService empresaService) {
        this.integracaoIfoodService = integracaoIfoodService;
        this.vendaRepository = vendaRepository;
        this.integracaoService = integracaoService;
        this.relatorioService = relatorioService;
        this.empresaService = empresaService;
    }

    @Override
    public List<Venda> buscar(Empresa empresa, Operadora operadora, LocalDate dtInicial, LocalDate dtFinal, String metodoPagamento, String bandeira, String tipoRecebimento) {

        if (empresa == null) {
            throw new NotificacaoException("Deve ser informada uma empresa.");
        }

        if (operadora == null) {
            throw new NotificacaoException("Deve ser informada uma operadora.");
        }

        if (dtInicial == null) {
            throw new NotificacaoException("Deve ser informada uma data inicial.");
        }

        if (dtFinal == null) {
            throw new NotificacaoException("Deve ser informada uma data final.");
        }

        List<Venda> vendas = vendaRepository.buscarPor(empresa, operadora, dtInicial, dtFinal);

        if (vendas == null || vendas.isEmpty()) {
            Integracao integracao = integracaoService.pesquisar(empresa, operadora);
            vendas = integracaoIfoodService.pesquisarVendas(integracao.getCodigoIntegracao(), dtInicial, dtFinal);
        }

        return filtrarVendas(vendas, metodoPagamento, bandeira, tipoRecebimento);
    }

    private List<Venda> filtrarVendas(final List<Venda> vendas, final String metodoPagamento, final String bandeira, final String tipoRecebimento) {
        return new VendaFilter(vendas, bandeira, metodoPagamento, tipoRecebimento)
                .porBandeira()
                .porMetodoPagamento()
                .porMetodoPagamentoBandeira()
                .porTipoRecebimento()
                .getVendasFiltradas();
    }

    @Override
    public byte[] gerarDadosCSV(LocalDate dataInicial, LocalDate dataFinal, Empresa empresa, Operadora operadora) {
        empresa = empresaService.pesquisarPorId(empresa.getId());
        RelatorioDTO relatorioDTO;

        try {
            relatorioDTO = relatorioService.gerarDados(TipoRelatorio.VENDA, vendaRepository, dataInicial, dataFinal, empresa, operadora);
        } catch (NotificacaoException e) {
            return new byte[0];
        }

        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Data pedido;Número documento;Razão social;Forma pagamento;Responsável;Valor bruto;Valor parcial;Valor cancelado;Valor comissão;Valor taxa entrega;Valor taxa serviço;Taxa comissão;Taxa comissão pagamento\n");

        for (RelatorioVendaDTO documento : relatorioDTO.getVendas()) {
            csvBuilder.append(documento.getDataPedido()).append(";")
                    .append(documento.getNumeroDocumento()).append(";")
                    .append(documento.getRazaoSocial()).append(";")
                    .append(documento.getFormaPagamento()).append(";")
                    .append(documento.getResponsavel()).append(";")
                    .append(documento.getValorBruto()).append(";")
                    .append(documento.getValorParcial()).append(";")
                    .append(documento.getValorCancelado()).append(";")
                    .append(documento.getValorComissao()).append(";")
                    .append(documento.getValorTaxaEntrega()).append(";")
                    .append(documento.getValorTaxaServico()).append(";")
                    .append(documento.getTaxaComissao()).append("%").append(";")
                    .append(documento.getTaxaComissaoPagamento()).append("%").append("\n");
        }

        return csvBuilder.toString().getBytes();
    }

    @Override
    public List<RelatorioVendaDTO> gerarDadosPDF(LocalDate dataInicial, LocalDate dataFinal, Empresa empresa, Operadora operadora) {
        empresa = empresaService.pesquisarPorId(empresa.getId());
        RelatorioDTO relatorioDTO = relatorioService.gerarDados(TipoRelatorio.VENDA, vendaRepository, dataInicial, dataFinal, empresa, operadora);
        return relatorioDTO.getVendas();
    }
}

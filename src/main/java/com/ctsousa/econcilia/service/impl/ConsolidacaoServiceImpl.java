package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.enumaration.TipoRelatorio;
import com.ctsousa.econcilia.mapper.impl.ConsolidadoMapper;
import com.ctsousa.econcilia.model.Consolidado;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.dto.ConsolidadoDTO;
import com.ctsousa.econcilia.model.dto.RelatorioConsolidadoDTO;
import com.ctsousa.econcilia.model.dto.RelatorioDTO;
import com.ctsousa.econcilia.processor.Processador;
import com.ctsousa.econcilia.repository.ConsolidadoRepository;
import com.ctsousa.econcilia.service.ConsolidacaoService;
import com.ctsousa.econcilia.service.EmpresaService;
import com.ctsousa.econcilia.service.RelatorioService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Component
public class ConsolidacaoServiceImpl implements ConsolidacaoService {

    private final ConsolidadoMapper mapper;

    private final ConsolidadoRepository consolidadoRepository;

    private final EmpresaService empresaService;

    private final RelatorioService relatorioService;

    public ConsolidacaoServiceImpl(ConsolidadoMapper mapper, ConsolidadoRepository consolidadoRepository, EmpresaService empresaService, RelatorioService relatorioService) {
        this.mapper = mapper;
        this.consolidadoRepository = consolidadoRepository;
        this.empresaService = empresaService;
        this.relatorioService = relatorioService;
    }

    @Override
    public void consolidar(Processador processador) {
        if (naoConsolidado(processador)) {
            ConsolidadoDTO consolidadoDTO = ConsolidadoDTO.builder()
                    .totalBruto(processador.getValorTotalBruto())
                    .ticketMedio(processador.getValorTotalTicketMedio())
                    .totalCancelado(processador.getValorTotalCancelado())
                    .totalTransacaoPagamento(processador.getValorTotalComissaoTransacaoPagamento())
                    .totalComissao(processador.getValorTotalComissao())
                    .totalTaxaEntrega(processador.getValorTotalTaxaEntrega())
                    .totalPromocao(processador.getValorTotalPromocao())
                    .totalLiquido(processador.getValorTotalLiquido())
                    .totalRecebido(processador.getValorTotalRecebido())
                    .quantidadeVenda(BigInteger.valueOf(processador.getQuantidade()))
                    .totalTaxaServico(processador.getValorTotalTaxaService())
                    .totalRepasse(processador.getValorTotalRepasse())
                    .totalTaxaManutencao(processador.getValorTotalManutencao())
                    .empresaId(processador.getEmpresa().getId())
                    .operadoraId(processador.getOperadora().getId())
                    .periodo(processador.getPeriodo())
                    .build();

            Consolidado consolidado = mapper.paraEntidade(consolidadoDTO);

            consolidadoRepository.save(consolidado);
        }
    }

    @Override
    public BigDecimal buscarValorBruto(Empresa empresa, Operadora operadora, LocalDate dataInicial, LocalDate dataFinal) {
        BigDecimal valorBruto = consolidadoRepository.findValorBruto(empresa, operadora, dataInicial, dataFinal);
        return valorBruto == null ? BigDecimal.valueOf(0D) : valorBruto;
    }

    @Override
    public boolean temMensalidade(Empresa empresa, Operadora operadora, LocalDate dataInicial, LocalDate dataFinal) {
        return consolidadoRepository.existsTaxaManutencao(empresa, operadora, dataInicial, dataFinal);
    }

    @Override
    public byte[] gerarDadosCSV(LocalDate dataInicial, LocalDate dataFinal, Empresa empresa, Operadora operadora) {
        empresa = empresaService.pesquisarPorId(empresa.getId());
        RelatorioDTO relatorioDTO = relatorioService.gerarDados(TipoRelatorio.CONSOLIDACAO, consolidadoRepository,dataInicial, dataFinal, empresa, operadora);

        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Período da venda;Nome cliente;Quantidade vendas;Total bruto;Ticket médio;Valor antecipado;Taxa entrega;Promoção;Transação pagamento;Comissão;Cancelamento;Taxa serviço;Taxa manutenção;Repasse\n");

        for (RelatorioConsolidadoDTO documento : relatorioDTO.getConsolidados()) {
            csvBuilder.append(documento.getPeriodo() == null ? "" : documento.getPeriodo()).append(";")
                    .append(empresa.getRazaoSocial()).append(";")
                    .append(documento.getQuantidadeVenda()).append(";")
                    .append(documento.getTotalBruto()).append(";")
                    .append(documento.getTicketMedio()).append(";")
                    .append(documento.getValorAntecipado()).append(";")
                    .append(documento.getTotalTaxaEntrega()).append(";")
                    .append(documento.getTotalPromocao()).append(";")
                    .append(documento.getTotalTransacaoPagamento()).append(";")
                    .append(documento.getTotalComissao()).append(";")
                    .append(documento.getTotalCancelado()).append(";")
                    .append(documento.getTotalTaxaServico()).append(";")
                    .append(documento.getTotalTaxaManutencao()).append(";")
                    .append(documento.getTotalRepasse()).append("\n");
        }

        return csvBuilder.toString().getBytes();
    }

    @Override
    public List<RelatorioConsolidadoDTO> gerarDadosPDF(LocalDate dataInicial, LocalDate dataFinal, Empresa empresa, Operadora operadora) {
        empresa = empresaService.pesquisarPorId(empresa.getId());
        RelatorioDTO relatorioDTO = relatorioService.gerarDados(TipoRelatorio.CONSOLIDACAO, consolidadoRepository, dataInicial, dataFinal, empresa, operadora);
        return relatorioDTO.getConsolidados();
    }

    private boolean naoConsolidado(final Processador processador) {
        return !consolidadoRepository.existsConsolidacao(processador.getEmpresa(), processador.getOperadora(), processador.getPeriodo());
    }
}

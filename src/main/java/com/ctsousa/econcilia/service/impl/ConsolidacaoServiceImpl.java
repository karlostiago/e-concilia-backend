package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.mapper.impl.ConsolidadoMapper;
import com.ctsousa.econcilia.model.Consolidado;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.dto.ConsolidadoDTO;
import com.ctsousa.econcilia.processor.Processador;
import com.ctsousa.econcilia.repository.ConsolidadoRepository;
import com.ctsousa.econcilia.service.ConsolidacaoService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import static com.ctsousa.econcilia.util.DecimalUtil.monetarioPtBr;

@Component
public class ConsolidacaoServiceImpl implements ConsolidacaoService {

    private final ConsolidadoMapper mapper;

    private final ConsolidadoRepository consolidadoRepository;

    public ConsolidacaoServiceImpl(ConsolidadoMapper mapper, ConsolidadoRepository consolidadoRepository) {
        this.mapper = mapper;
        this.consolidadoRepository = consolidadoRepository;
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
    public byte[] gerarCSV(LocalDate dataInicial, LocalDate dataFinal, Empresa empresa, Operadora operadora) {
        List<Object[]> consolidados = consolidadoRepository.por(dataInicial, dataFinal, empresa.getId(), operadora.getId());

        if (consolidados.isEmpty()) return new byte[0];

        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Período da venda;Nome cliente;Quantidade vendas;Total bruto;Ticket médio;Valor antecipado;Taxa entrega;Promoção;Transação pagamento;Comissão;Cancelamento;Taxa serviço;Taxa manutenção;Repasse\n");

        for (Object [] consolidado : consolidados) {
            csvBuilder.append(consolidado[0]).append(";")
                    .append(consolidado[1]).append(";")
                    .append(monetarioPtBr((BigDecimal) consolidado[2])).append(";")
                    .append(monetarioPtBr((BigDecimal) consolidado[3])).append(";")
                    .append(monetarioPtBr((BigDecimal) consolidado[4])).append(";")
                    .append(monetarioPtBr((BigDecimal) consolidado[5])).append(";")
                    .append(monetarioPtBr((BigDecimal) consolidado[6])).append(";")
                    .append(monetarioPtBr((BigDecimal) consolidado[7])).append(";")
                    .append(monetarioPtBr((BigDecimal) consolidado[8])).append(";")
                    .append(monetarioPtBr((BigDecimal) consolidado[9])).append(";")
                    .append(monetarioPtBr((BigDecimal) consolidado[10])).append(";")
                    .append(monetarioPtBr((BigDecimal) consolidado[11])).append(";")
                    .append(monetarioPtBr((BigDecimal) consolidado[12])).append(";")
                    .append(monetarioPtBr((BigDecimal) consolidado[13])).append("\n");
        }

        return csvBuilder.toString().getBytes();
    }

    private boolean naoConsolidado(final Processador processador) {
        return !consolidadoRepository.existsConsolidacao(processador.getEmpresa(), processador.getOperadora(), processador.getPeriodo());
    }
}

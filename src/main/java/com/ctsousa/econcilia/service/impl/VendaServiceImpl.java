package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.filter.VendaFilter;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.repository.VendaRepository;
import com.ctsousa.econcilia.service.IntegracaoIfoodService;
import com.ctsousa.econcilia.service.IntegracaoService;
import com.ctsousa.econcilia.service.VendaService;
import com.ctsousa.econcilia.util.DataUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static com.ctsousa.econcilia.util.DecimalUtil.monetarioPtBr;

@Component
public class VendaServiceImpl implements VendaService {

    private final IntegracaoIfoodService integracaoIfoodService;

    private final VendaRepository vendaRepository;

    private final IntegracaoService integracaoService;

    public VendaServiceImpl(IntegracaoIfoodService integracaoIfoodService, VendaRepository vendaRepository, IntegracaoService integracaoService) {
        this.integracaoIfoodService = integracaoIfoodService;
        this.vendaRepository = vendaRepository;
        this.integracaoService = integracaoService;
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
    public byte[] gerarCSV(LocalDate dataInicial, LocalDate dataFinal, Empresa empresa, Operadora operadora) {
        List<Object[]> vendas = vendaRepository.por(dataInicial, dataFinal, empresa.getId(), operadora.getId());

        if (vendas.isEmpty()) return new byte[0];

        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Data pedido;Número documento;Razão social;Forma pagamento;Responsável;Valor bruto;Valor parcial;Valor cancelado;Valor comissão;Valor taxa entrega;Valor taxa serviço;Taxa comissão;Taxa comissão pagamento\n");

        for (Object [] venda : vendas) {
            csvBuilder.append(DataUtil.paraPtBr(((Date) venda[0]).toLocalDate())).append(";")
                    .append(venda[1]).append(";")
                    .append(venda[2]).append(";")
                    .append(venda[3]).append(";")
                    .append(venda[4]).append(";")
                    .append(monetarioPtBr((BigDecimal) venda[5])).append(";")
                    .append(monetarioPtBr((BigDecimal)venda[6])).append(";")
                    .append(monetarioPtBr((BigDecimal)venda[7])).append(";")
                    .append(monetarioPtBr((BigDecimal)venda[8])).append(";")
                    .append(monetarioPtBr((BigDecimal)venda[9])).append(";")
                    .append(monetarioPtBr((BigDecimal)venda[10])).append(";")
                    .append(monetarioPtBr(((BigDecimal)venda[11]).multiply((BigDecimal.valueOf(100D))))).append("%").append(";")
                    .append(monetarioPtBr(((BigDecimal)venda[12]).multiply(BigDecimal.valueOf(100D)))).append("%").append("\n");
        }

        return csvBuilder.toString().getBytes();
    }
}

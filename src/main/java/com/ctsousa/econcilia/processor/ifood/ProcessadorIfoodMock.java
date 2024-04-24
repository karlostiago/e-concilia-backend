package com.ctsousa.econcilia.processor.ifood;

import com.ctsousa.econcilia.model.Cobranca;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Pagamento;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.processor.Processador;
import com.ctsousa.econcilia.processor.ProcessadorFiltro;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

@Slf4j
@Profile("test")
public class ProcessadorIfoodMock extends Processador {

    private static final Random random = new Random();

    @Override
    public void processar(ProcessadorFiltro processadorFiltro, boolean executarCalculo, boolean consolidar) {
        vendas = new ArrayList<>(1);

        Pagamento pagamento = new Pagamento();
        pagamento.setMetodo("cash");

        vendas.add(criarVenda(pagamento, BigDecimal.valueOf(1100D), BigDecimal.valueOf(0.99D), BigDecimal.valueOf(3D)));

        if (executarCalculo) {
            calcularValoresMock();
        }

        log.info("processamento realizado com sucesso");
    }

    private void calcularValoresMock() {
        valorTotalBruto = BigDecimal.valueOf(random.nextDouble() * 1000);
        valorTotalTicketMedio = BigDecimal.valueOf(random.nextDouble() * 1000);
        valorTotalCancelado = BigDecimal.valueOf(random.nextDouble() * 1000);
        valorTotalRecebido = BigDecimal.valueOf(random.nextDouble() * 1000);
        valorTotalComissaoTransacaoPagamento = BigDecimal.valueOf(random.nextDouble() * 1000);
        valorTotalComissao = BigDecimal.valueOf(random.nextDouble() * 1000);
        valorTotalTaxaEntrega = BigDecimal.valueOf(random.nextDouble() * 1000);
        valorTotalRepasse = BigDecimal.valueOf(random.nextDouble() * 1000);
        valorTotalPromocao = BigDecimal.valueOf(random.nextDouble() * 1000);
        valorTotalPedido = BigDecimal.valueOf(random.nextDouble() * 1000);
        valorTotalLiquido = BigDecimal.valueOf(random.nextDouble() * 1000);
        quantidade = 10;
    }

    private Venda criarVenda(Pagamento pagamento, BigDecimal valorBruto, BigDecimal taxaServico, BigDecimal taxaEntrega) {
        Cobranca cobranca = new Cobranca();
        cobranca.setValorBruto(valorBruto);
        cobranca.setTaxaServico(taxaServico);
        cobranca.setTaxaEntrega(taxaEntrega);
        cobranca.setTaxaComissaoAdquirente(BigDecimal.valueOf(5D));
        cobranca.setTaxaComissao(BigDecimal.valueOf(3D));

        Empresa empresa = new Empresa(1L);
        empresa.setRazaoSocial("EMPRESA TESTE");

        Venda venda = new Venda();
        venda.setDataPedido(LocalDate.now().minusDays(1));
        venda.setCobranca(cobranca);
        venda.setEmpresa(empresa);
        venda.setPagamento(pagamento);
        return venda;
    }
}

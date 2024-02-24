package com.ctsousa.econcilia;

import com.ctsousa.econcilia.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ApplicationUnitTest {

    protected List<Venda> getVendas() {
        List<Venda> vendas = new ArrayList<>(2);

        vendas.add(criarVenda(getPagamento("cash"), "1", BigDecimal.valueOf(1100D), BigDecimal.valueOf(0.99D), BigDecimal.valueOf(3D)));
        vendas.add(criarVenda(getPagamento("pix"), "2", BigDecimal.valueOf(740D), BigDecimal.valueOf(0.99D), BigDecimal.valueOf(3.5D)));
        vendas.add(criarVenda(getPagamento("credit"), "3", BigDecimal.valueOf(800D), BigDecimal.valueOf(0.99D), BigDecimal.valueOf(2D)));
        vendas.add(criarVenda(getPagamento("debit"), "4", BigDecimal.valueOf(1000D), BigDecimal.valueOf(0.99D), BigDecimal.valueOf(3D)));
        vendas.add(criarVenda(getPagamento("voucher_ifood"), "5", BigDecimal.valueOf(200D), BigDecimal.valueOf(0D), BigDecimal.valueOf(2D)));

        return vendas;
    }

    protected List<AjusteVenda> getAjusteVendas() {
        List<AjusteVenda> ajusteVendas = new ArrayList<>(2);
        ajusteVendas.add(criarAjusteVenda("3"));
        ajusteVendas.add(criarAjusteVenda("4"));
        return ajusteVendas;
    }

    private AjusteVenda criarAjusteVenda(String pedidoId) {
        AjusteVenda ajusteVenda = new AjusteVenda();
        ajusteVenda.setCobranca(new Cobranca());
        ajusteVenda.setPedidoId(pedidoId);
        return ajusteVenda;
    }

    private Pagamento getPagamento(String metodo) {
        Pagamento pagamento = new Pagamento();
        pagamento.setBandeira("VISA");
        pagamento.setMetodo(metodo.toUpperCase());
        pagamento.setNumeroCartao("3091");
        pagamento.setResponsavel("IFOOD");
        pagamento.setTipo("ONLINE");
        return pagamento;
    }

    protected Venda criarVenda(Pagamento pagamento, String pedidoId, BigDecimal valorBruto, BigDecimal taxaServico, BigDecimal taxaEntrega) {
        Cobranca cobranca = new Cobranca();
        cobranca.setValorBruto(valorBruto);
        cobranca.setTaxaServico(taxaServico);
        cobranca.setTaxaEntrega(taxaEntrega);

        Empresa empresa = new Empresa(1L);
        empresa.setRazaoSocial("EMPRESA TESTE");

        Operadora operadora = new Operadora(1L);
        operadora.setDescricao("Operadora");

        Venda venda = new Venda();
        venda.setPedidoId(pedidoId);
        venda.setDataPedido(LocalDate.now().minusDays(1));
        venda.setCobranca(cobranca);
        venda.setEmpresa(empresa);
        venda.setOperadora(operadora);
        venda.setPagamento(pagamento);
        venda.setConciliado(true);
        venda.setDiferenca(BigDecimal.valueOf(0D));
        venda.setModeloNegocio("MODELO TEST");
        venda.setNumeroDocumento("123");
        venda.setRazaoSocial(empresa.getRazaoSocial());
        venda.setPeriodoId("123456");
        return venda;
    }
}

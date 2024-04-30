package com.ctsousa.econcilia.repository;

import com.ctsousa.econcilia.model.AjusteVenda;
import com.ctsousa.econcilia.model.Cobranca;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.ctsousa.econcilia.util.DataUtil.paraLocalDate;

@Repository
public interface AjusteVendaRepository extends JpaRepository<AjusteVenda, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM AjusteVenda a WHERE YEAR(a.dataPedido) = YEAR(:dataPedido) AND MONTH(a.dataPedido) = MONTH(:dataPedido)")
    void deleteAjusteVendas(@Param(value = "dataPedido") LocalDate dataPedido);

    @Query(value = "SELECT DISTINCT " +
            "   av.id AS ajuste_id, " +
            "   av.data_pagamento_esperada, " +
            "   av.data_pedido, " +
            "   av.data_pedido_atualizado, " +
            "   av.numero_documento, " +
            "   av.pedido_cobranca_id, " +
            "   av.pedido_id, " +
            "   av.periodo_id, " +
            "   av.cobranca_id, " +
            "   av.valor_ajuste, " +
            "   cob.* " +
            "   FROM ajuste_venda av " +
            "INNER JOIN venda v ON v.periodo_id = av.periodo_id " +
            "INNER JOIN cobranca cob ON cob.id = av.cobranca_id  " +
            "INNER JOIN integracao i ON i.empresa_id = v.empresa_id AND i.operadora_id = v.operadora_id " +
            "WHERE av.data_pedido BETWEEN :dataInicial AND :dataFinal " +
            "  AND i.empresa_id = :empresa " +
            "  AND i.operadora_id = :operadora " +
            "  AND i.codigo_integracao = :codigoIntegracao ",
            nativeQuery = true)
    List<Object[]> buscar(@Param(value = "empresa") Long empresa,
                             @Param(value = "operadora") Long operadora,
                             @Param(value = "dataInicial") LocalDate dataInicial,
                             @Param(value = "dataFinal") LocalDate dataFinal,
                             @Param(value = "codigoIntegracao") String codigoIntegracao);

    default List<AjusteVenda> buscar(Empresa empresa, Operadora operadora, LocalDate dataInicial, LocalDate dataFinal, String codigoIntegracao) {
        List<Object[]> result = buscar(empresa.getId(), operadora.getId(), dataInicial, dataFinal, codigoIntegracao);
        List<AjusteVenda> ajusteVendas = new ArrayList<>();
        for (Object[] obj : result) {
            AjusteVenda ajusteVenda = new AjusteVenda();
            ajusteVenda.setId(((BigInteger) obj[0]).longValue());
            ajusteVenda.setDataPedido(paraLocalDate(String.valueOf(obj[2])));
            ajusteVenda.setDataPedidoAtualizado(paraLocalDate(String.valueOf(obj[3])));
            ajusteVenda.setNumeroDocumento(String.valueOf(obj[4]));
            ajusteVenda.setPedidoCobrancaId(String.valueOf(obj[5]));
            ajusteVenda.setPedidoId(String.valueOf(obj[6]));
            ajusteVenda.setPeriodoId(String.valueOf(obj[7]));
            ajusteVenda.setCobranca(inicializar(obj));
            ajusteVenda.setValorAjuste((BigDecimal)obj[9]);
            ajusteVendas.add(ajusteVenda);
        }
        return ajusteVendas;
    }

    private Cobranca inicializar(Object[] obj) {
        Cobranca cobranca = new Cobranca();
        cobranca.setId(((BigInteger)obj[10]).longValue());
        cobranca.setBeneficioComercio((BigDecimal)obj[11]);
        cobranca.setBeneficioOperadora((BigDecimal)obj[12]);
        cobranca.setComissao((BigDecimal)obj[13]);
        cobranca.setComissaoEntrega((BigDecimal)obj[14]);
        cobranca.setTaxaAdquirente((BigDecimal)obj[15]);
        cobranca.setTaxaAdquirenteAplicada((BigDecimal)obj[15]);
        cobranca.setTaxaAntecipacao((BigDecimal)obj[17]);
        cobranca.setTaxaComissao((BigDecimal)obj[18]);
        cobranca.setTaxaComissaoAdquirente((BigDecimal)obj[19]);
        cobranca.setTaxaEntrega((BigDecimal)obj[20]);
        cobranca.setTaxaServico((BigDecimal)obj[21]);
        cobranca.setTotalCredito((BigDecimal)obj[22]);
        cobranca.setTotalDebito((BigDecimal)obj[23]);
        cobranca.setValorBruto((BigDecimal)obj[24]);
        cobranca.setValorCancelado((BigDecimal)obj[25]);
        cobranca.setValorParcial((BigDecimal)obj[26]);
        cobranca.setValorTaxaAntecipacao((BigDecimal)obj[27]);
        cobranca.setTaxaAdquirenteBeneficio((BigDecimal)obj[28]);
        return cobranca;
    }
}

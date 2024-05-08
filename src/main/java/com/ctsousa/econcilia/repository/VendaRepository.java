package com.ctsousa.econcilia.repository;

import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    @Query(value = "SELECT v FROM Venda v INNER JOIN FETCH v.cobranca INNER JOIN FETCH v.empresa emp INNER JOIN FETCH v.operadora oper INNER JOIN FETCH v.pagamento WHERE v.empresa = :empresa AND v.operadora = :operadora AND (v.periodoId IS NULL OR v.periodoId = '') ORDER BY v.dataPedido")
    List<Venda> buscarVendasSemPeriodoId(Empresa empresa, Operadora operadora);

    @Query(value = "SELECT v FROM Venda v INNER JOIN FETCH v.cobranca INNER JOIN FETCH v.empresa emp INNER JOIN FETCH v.operadora oper INNER JOIN FETCH v.pagamento WHERE v.empresa = :empresa AND v.operadora = :operadora AND v.dataPedido BETWEEN :dtInicial AND :dtFinal")
    List<Venda> buscarPor(Empresa empresa, Operadora operadora, LocalDate dtInicial, LocalDate dtFinal);

    @Query(value = "SELECT v FROM Venda v INNER JOIN FETCH v.cobranca INNER JOIN FETCH v.empresa emp INNER JOIN FETCH v.operadora oper INNER JOIN FETCH v.pagamento WHERE v.empresa = :empresa AND v.dataPedido BETWEEN :dtInicial AND :dtFinal")
    List<Venda> buscarPor(Empresa empresa, LocalDate dtInicial, LocalDate dtFinal);

    @Query("SELECT COUNT(v) > 0 FROM Venda v WHERE v.empresa = :empresa AND v.operadora = :operadora AND YEAR(v.dataPedido) = YEAR(:periodo) AND MONTH(v.dataPedido) = MONTH(:periodo)")
    Boolean existsVenda(@Param(value = "empresa") Empresa empresa,
                               @Param(value = "operadora") Operadora operadora,
                               @Param(value = "periodo") LocalDate periodo);

    @Modifying
    @Transactional
    @Query("DELETE FROM Venda v WHERE YEAR(v.dataPedido) = YEAR(:dataPedido) AND MONTH(v.dataPedido) = MONTH(:dataPedido) AND v.operadora = :operadora AND v.empresa = :empresa")
    void deleteVendas(@Param(value = "empresa") Empresa empresa,
                      @Param(value = "operadora") Operadora operadora,
                      @Param(value = "dataPedido") LocalDate dataPedido);

    @Modifying
    @Transactional
    @Query("DELETE FROM Cobranca c WHERE c.id IN (:listCobrancaId)")
    void deleteCobrancas(@Param(value = "listCobrancaId") List<Long> listCobrancaId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Pagamento p WHERE p.id IN (:listPagamentoId)")
    void deletePagamentos(@Param(value = "listPagamentoId") List<Long> listPagamentoId);

    @Query(value = "SELECT " +
            "   v.data_pedido AS dataPedido, " +
            "   v.numero_documento AS numeroDocumento, " +
            "   v.razao_social AS razaoSocial, " +
            "   CASE WHEN p.metodo = 'CREDIT' THEN 'CREDITO' " +
            "        WHEN p.metodo = 'DEBITO' THEN 'DEBITO' " +
            "        WHEN p.metodo = 'PIX' THEN 'PIX' " +
            "        WHEN p.metodo = 'CASH' THEN 'DINHEIRO' " +
            "        ELSE p.metodo END AS formaPagamento, " +
            "   CASE WHEN p.responsavel = 'IFOOD' THEN 'OPERADORA' ELSE 'LOJA' END AS responsavel, " +
            "   SUM(c.valor_parcial + c.taxa_entrega + c.taxa_servico) AS valorBruto, " +
            "   SUM(c.valor_parcial) AS valorParcial, " +
            "   SUM(COALESCE(cancel.valor, 0)) AS valorCancelado, " +
            "   SUM(c.comissao) AS valorComissao, " +
            "   SUM(c.taxa_entrega) AS taxaEntrega, " +
            "   SUM(c.taxa_servico) AS taxaServico, " +
            "   c.taxa_comissao AS taxaComissao, " +
            "   c.taxa_comissao_adquirente AS taxaComissaoPagamento " +
            "FROM venda v " +
            "INNER JOIN empresa e ON e.id = v.empresa_id " +
            "INNER JOIN operadora o ON o.id = v.operadora_id " +
            "INNER JOIN cobranca c ON c.id = v.cobranca_id " +
            "INNER JOIN pagamento p ON p.id = v.pagamento_id " +
            "LEFT JOIN cancelamento cancel ON cancel.pedido_id = v.pedido_id " +
            "WHERE v.data_pedido BETWEEN :dataInicial AND :dataFinal " +
            "  AND v.empresa_id = :empresaId " +
            "  AND v.operadora_id = :operadoraId " +
            "GROUP BY v.data_pedido, v.numero_documento, v.razao_social, c.taxa_comissao, c.taxa_comissao_adquirente, p.metodo, p.responsavel " +
            "ORDER BY v.data_pedido ASC ",
            nativeQuery = true)
    List<Object[]> por(@Param(value = "dataInicial") LocalDate dataInicial,
                                @Param(value = "dataFinal") LocalDate dataFinal,
                                @Param(value = "empresaId") Long empresaId,
                                @Param(value = "operadoraId") Long operadoraId);
}

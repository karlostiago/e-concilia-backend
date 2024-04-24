package com.ctsousa.econcilia.repository;

import com.ctsousa.econcilia.model.Consolidado;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConsolidadoRepository extends JpaRepository<Consolidado, Long> {

    @Query("SELECT SUM(c.totalBruto) FROM Consolidado c WHERE c.empresa = :empresa AND c.operadora = :operadora AND c.periodo BETWEEN :periodoInicial AND :periodoFinal")
    BigDecimal findValorBruto(@Param(value = "empresa") Empresa empresa,
                              @Param(value = "operadora") Operadora operadora,
                              @Param(value = "periodoInicial") LocalDate periodoInicial,
                              @Param(value = "periodoFinal") LocalDate periodoFinal);

    @Query("SELECT COUNT(c) > 0 FROM Consolidado c WHERE c.empresa = :empresa AND c.operadora = :operadora AND c.periodo = :periodo")
    Boolean existsConsolidacao(@Param(value = "empresa") Empresa empresa,
                               @Param(value = "operadora") Operadora operadora,
                               @Param(value = "periodo") LocalDate periodo);

    @Query("SELECT COALESCE(SUM(c.totalTaxaManutencao), 0) > 0 FROM Consolidado c WHERE c.empresa = :empresa AND c.operadora = :operadora AND c.periodo BETWEEN :periodoInicial AND :periodoFinal")
    Boolean existsTaxaManutencao(@Param(value = "empresa") Empresa empresa,
                                 @Param(value = "operadora") Operadora operadora,
                                 @Param(value = "periodoInicial") LocalDate periodoInicial,
                                 @Param(value = "periodoFinal") LocalDate periodoFinal);

    @Query(value = "SELECT " +
            "   DATE_FORMAT(c.periodo, '%m-%Y') AS PERIODO, " +
            "   emp.razao_social AS NOME_CLIENTE, " +
            "   SUM(c.quantidade_venda) AS QUANTIDADE_VENDAS, " +
            "   SUM(c.total_bruto) AS TOTAL_BRUTO, " +
            "   SUM(c.total_bruto) / SUM(quantidade_venda)  AS TICKET_MEDIO, " +
            "   SUM(c.total_recebido) AS VALOR_ANTECIPADO, " +
            "   SUM(c.total_taxa_entrega) AS TAXA_ENTREGA, " +
            "   SUM(c.total_promocao) AS PROMOCAO, " +
            "   SUM(c.total_transacao_pagamento) AS TRANSACAO_PAGAMENTO, " +
            "   SUM(c.total_comissao) AS COMISSAO, " +
            "   SUM(c.total_cancelado) AS CANCELAMENTO, " +
            "   SUM(c.total_taxa_servico * -1) AS TAXA_SERVICO, " +
            "   SUM(c.total_taxa_manutencao * -1) AS TAXA_MANUTENCAO, " +
            "   SUM(c.total_repasse) AS REPASSE " +
            "FROM consolidado c " +
            "INNER JOIN empresa emp ON emp.id = c.empresa_id  " +
            "WHERE c.periodo BETWEEN :dataInicial AND :dataFinal " +
            "  AND c.empresa_id = :empresaId " +
            "  AND c.operadora_id = :operadoraId " +
            "GROUP BY emp.razao_social, DATE_FORMAT(c.periodo, '%m-%Y') ",
            nativeQuery = true)
    List<Object[]> por(@Param(value = "dataInicial") LocalDate dataInicial,
                       @Param(value = "dataFinal") LocalDate dataFinal,
                       @Param(value = "empresaId") Long empresaId,
                       @Param(value = "operadoraId") Long operadoraId);
}

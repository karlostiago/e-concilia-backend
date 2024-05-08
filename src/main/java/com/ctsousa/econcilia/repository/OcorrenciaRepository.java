package com.ctsousa.econcilia.repository;

import com.ctsousa.econcilia.model.Ocorrencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, Long> {

    @Query(value = "SELECT o FROM Ocorrencia o WHERE o.periodoId IS NULL OR o.periodoId = ''")
    List<Ocorrencia> buscarOcorrenciaSemPeriodoId();

    @Modifying
    @Transactional
    @Query("DELETE FROM Ocorrencia o WHERE YEAR(o.dataTransacao) = YEAR(:dataTransacao) AND MONTH(o.dataTransacao) = MONTH(:dataTransacao)")
    void deleteOcorrencia(@Param(value = "dataTransacao") LocalDate dataTransacao);

    @Query(value = "SELECT DISTINCT occer.* FROM ocorrencia occer " +
            "INNER JOIN venda ven ON ven.periodo_id = occer.periodo_id " +
            "INNER JOIN integracao integ ON integ.empresa_id = ven.empresa_id AND integ.operadora_id = ven.operadora_id " +
            "WHERE occer.data_transacao BETWEEN :dataInicial AND :dataFinal " +
            "  AND integ.empresa_id = :empresa " +
            "  AND integ.operadora_id = :operadora " +
            "  AND ven.periodo_id = occer.periodo_id ",
            nativeQuery = true)
    List<Ocorrencia> buscar(@Param(value = "dataInicial") LocalDate dataInicial,
                            @Param(value = "dataFinal") LocalDate dataFinal,
                            @Param(value = "empresa") Long empresa,
                            @Param(value = "operadora") Long operadora);
}

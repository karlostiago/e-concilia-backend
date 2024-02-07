package com.ctsousa.econcilia.repository;

import com.ctsousa.econcilia.model.Taxa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaxaRepository extends JpaRepository<Taxa, Long> {

    @Query(value = "SELECT t FROM Taxa t INNER JOIN FETCH t.contrato c INNER JOIN FETCH c.empresa INNER JOIN c.operadora o WHERE c.id = :contratoId")
    List<Taxa> findByContrato(@Param(value = "contratoId") Long contratoId);

    @Query(value = "SELECT t FROM Taxa t INNER JOIN FETCH t.contrato c INNER JOIN FETCH c.empresa INNER JOIN c.operadora o WHERE o.id = :operadoraId")
    List<Taxa> findByOperadora(@Param(value = "operadoraId") Long operadoraId);

    @Query(value = "SELECT t FROM Taxa t INNER JOIN t.contrato c INNER JOIN c.empresa e INNER JOIN c.operadora o WHERE e.id = :empresaId")
    List<Taxa> findByEmpresa(@Param(value = "empresaId") Long empresaId);

    @Query(value = "SELECT t FROM Taxa t INNER JOIN FETCH t.contrato c INNER JOIN FETCH c.empresa INNER JOIN c.operadora o WHERE 1 = 1")
    List<Taxa> findByAll();

    @Query(value = "SELECT t FROM Taxa t INNER JOIN FETCH t.contrato c INNER JOIN FETCH c.empresa WHERE t.id = :id")
    Optional<Taxa> porId(@Param(value = "id") Long id);

    @Query(value = "SELECT tx.* FROM contrato c " +
            "INNER JOIN taxa tx ON tx.contrato_id = c.id " +
            "WHERE c.empresa_id = :empresaId " +
            "AND c.operadora_id = :operadoraId " +
            "AND UPPER(tx.descricao) LIKE :descricao% " +
            "AND tx.valor = :valor AND tx.ativo = 1 AND c.ativo = 1 " +
            "LIMIT 1", nativeQuery = true)
    Optional<Taxa> por(@Param(value = "empresaId") Long empresaId,
                       @Param(value = "operadoraId") Long operadoraId,
                       @Param(value = "descricao") String descricao,
                       @Param(value = "valor") BigDecimal valor);

    @Query(value = "SELECT tx.* FROM contrato c " +
            "INNER JOIN empresa emp ON c.empresa_id = emp.id " +
            "INNER JOIN operadora oper ON oper .id = c.operadora_id " +
            "INNER JOIN taxa tx ON tx.contrato_id = c.id " +
            "WHERE c.ativo = 1 AND tx.ativo = 1 AND UPPER(tx.descricao) LIKE :descricao% AND c.empresa_id = :empresaId",
    nativeQuery = true)
    Optional<Taxa> por(@Param(value = "descricao") String descricao, @Param(value = "empresaId") Long empresaId);
}

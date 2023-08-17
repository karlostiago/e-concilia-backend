package com.ctsousa.econcilia.repository;

import com.ctsousa.econcilia.model.Contrato;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Taxa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxaRepository extends JpaRepository<Taxa, Long> {

    @Query(value = "SELECT t FROM Taxa t WHERE t.contrato.id = :contratoId")
    List<Taxa> findByContrato(@Param(value = "contratoId") Long contratoId);

    @Query(value = "SELECT t FROM Taxa t INNER JOIN FETCH t.contrato c INNER JOIN FETCH c.empresa INNER JOIN c.operadora o WHERE o.id = :operadoraId")
    List<Taxa> findByOperadora(@Param(value = "operadoraId") Long operadoraId);

    @Query(value = "SELECT t FROM Taxa t INNER JOIN FETCH t.contrato c INNER JOIN FETCH c.empresa INNER JOIN c.operadora o WHERE 1 = 1")
    List<Taxa> findByAll();
}

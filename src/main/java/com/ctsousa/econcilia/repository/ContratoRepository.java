package com.ctsousa.econcilia.repository;

import com.ctsousa.econcilia.model.Contrato;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    @Query(value = "SELECT c FROM Contrato c INNER JOIN FETCH c.empresa INNER JOIN FETCH c.operadora WHERE c.id = :id")
    Contrato porID(@Param(value = "id") Long id);

    boolean existsByEmpresaAndOperadora(Empresa empresa, Operadora operadora);
}

package com.ctsousa.econcilia.repository;

import com.ctsousa.econcilia.model.Contrato;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    @Query(value = "SELECT c FROM Contrato c INNER JOIN FETCH c.empresa INNER JOIN FETCH c.operadora INNER JOIN FETCH c.taxas WHERE c.id = :id")
    Optional<Contrato> porID(@Param(value = "id") Long id);

    @Query(value = "SELECT c FROM Contrato c INNER JOIN FETCH c.empresa INNER JOIN FETCH c.operadora WHERE c.operadora = :operadora")
    List<Contrato> findByOperadora(@Param(value = "operadora") Operadora operadora);

    @Query(value = "SELECT c FROM Contrato c INNER JOIN FETCH c.empresa INNER JOIN FETCH c.operadora WHERE c.empresa = :empresa")
    List<Contrato> findByEmpresa(@Param(value = "empresa") Empresa empresa);

    @Query(value = "SELECT c FROM Contrato c INNER JOIN FETCH c.empresa INNER JOIN FETCH c.operadora WHERE c.empresa = :empresa AND c.operadora = :operadora")
    List<Contrato> findByEmpresaAndOperadora(@Param(value = "empresa") Empresa empresa, @Param(value = "operadora") Operadora operadora);

    @Query(value = "SELECT c FROM Contrato c INNER JOIN FETCH c.empresa INNER JOIN FETCH c.operadora")
    List<Contrato> findAll();

    boolean existsByEmpresaAndOperadora(Empresa empresa, Operadora operadora);
}

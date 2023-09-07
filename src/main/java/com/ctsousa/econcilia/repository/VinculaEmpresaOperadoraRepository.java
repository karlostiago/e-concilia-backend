package com.ctsousa.econcilia.repository;

import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.VinculaEmpresaOperadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VinculaEmpresaOperadoraRepository extends JpaRepository<VinculaEmpresaOperadora, Long> {

    @Query(value = "SELECT v FROM VinculaEmpresaOperadora v INNER JOIN FETCH v.empresa INNER JOIN FETCH v.operadora WHERE v.id = :id")
    Optional<VinculaEmpresaOperadora> porID(@Param(value = "id") Long id);

    @Query(value = "SELECT v FROM VinculaEmpresaOperadora v INNER JOIN FETCH v.empresa INNER JOIN FETCH v.operadora")
    List<VinculaEmpresaOperadora> findAll();

    @Query(value = "SELECT v FROM VinculaEmpresaOperadora v INNER JOIN FETCH v.empresa INNER JOIN FETCH v.operadora WHERE v.empresa = :empresa")
    List<VinculaEmpresaOperadora> findByEmpresa(@Param(value = "empresa") Empresa empresa);

    @Query(value = "SELECT v FROM VinculaEmpresaOperadora v INNER JOIN FETCH v.empresa INNER JOIN FETCH v.operadora WHERE v.operadora = :operadora")
    List<VinculaEmpresaOperadora> findByOperadora(@Param(value = "operadora") Operadora operadora);

    @Query(value = "SELECT v FROM VinculaEmpresaOperadora v INNER JOIN FETCH v.empresa INNER JOIN FETCH v.operadora WHERE v.codigoIntegracao = :codigoIntegracao")
    Optional<VinculaEmpresaOperadora> findByCodigoIntegracao(@Param(value = "codigoIntegracao") String codigoIntegracao);

    @Query(value = "SELECT v FROM VinculaEmpresaOperadora v INNER JOIN FETCH v.empresa INNER JOIN FETCH v.operadora WHERE v.empresa = :empresa AND v.operadora = :operadora")
    List<VinculaEmpresaOperadora> findByEmpresaAndOperadora(@Param(value = "empresa") Empresa empresa, @Param(value = "operadora") Operadora operadora);

    boolean existsByOperadoraAndCodigoIntegracao(Operadora operadora, String codigoIntegracao);
}

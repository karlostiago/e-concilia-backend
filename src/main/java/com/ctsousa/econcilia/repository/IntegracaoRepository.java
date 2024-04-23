package com.ctsousa.econcilia.repository;

import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Operadora;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IntegracaoRepository extends JpaRepository<Integracao, Long> {

    @Query(value = "SELECT i FROM Integracao i INNER JOIN FETCH i.empresa INNER JOIN FETCH i.operadora WHERE i.id = :id")
    Optional<Integracao> porID(@Param(value = "id") Long id);

    @NonNull
    @Query(value = "SELECT i FROM Integracao i INNER JOIN FETCH i.empresa INNER JOIN FETCH i.operadora")
    List<Integracao> findAll();

    @Query(value = "SELECT i FROM Integracao i INNER JOIN FETCH i.empresa INNER JOIN FETCH i.operadora WHERE i.empresa = :empresa")
    List<Integracao> findByEmpresa(@Param(value = "empresa") Empresa empresa);

    @Query(value = "SELECT i FROM Integracao i INNER JOIN FETCH i.empresa INNER JOIN FETCH i.operadora WHERE i.operadora = :operadora")
    List<Integracao> findByOperadora(@Param(value = "operadora") Operadora operadora);

    @Query(value = "SELECT i FROM Integracao i INNER JOIN FETCH i.empresa INNER JOIN FETCH i.operadora WHERE i.codigoIntegracao = :codigoIntegracao")
    Optional<Integracao> findByCodigoIntegracao(@Param(value = "codigoIntegracao") String codigoIntegracao);

    @Query(value = "SELECT i FROM Integracao i INNER JOIN FETCH i.empresa INNER JOIN FETCH i.operadora WHERE i.empresa = :empresa AND i.operadora = :operadora")
    List<Integracao> findByEmpresaAndOperadora(@Param(value = "empresa") Empresa empresa, @Param(value = "operadora") Operadora operadora);

    boolean existsByOperadoraAndCodigoIntegracao(Operadora operadora, String codigoIntegracao);
}

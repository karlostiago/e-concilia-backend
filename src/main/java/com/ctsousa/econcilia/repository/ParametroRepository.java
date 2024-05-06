package com.ctsousa.econcilia.repository;

import com.ctsousa.econcilia.enumaration.TipoParametro;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Parametro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParametroRepository extends JpaRepository<Parametro, Long> {

    Parametro findByTipoParametro(@Param("tipoParametro") TipoParametro tipoParametro);

    @Query("SELECT p FROM Parametro p WHERE p.tipoParametro = :tipoParametro AND p.empresa = :empresa AND p.operadora = :operadora")
    Parametro buscaParametroTipoEmpresaOperadora(@Param("tipoParametro") TipoParametro tipoParametro, @Param("empresa") Empresa empresa, @Param("operadora") Operadora operadora);

    @Query("SELECT p FROM Parametro p INNER JOIN FETCH p.empresa INNER JOIN FETCH p.operadora WHERE p.empresa = :empresa AND p.operadora = :operadora")
    List<Parametro> buscaParametroEmpresaOperadora(@Param("empresa") Empresa empresa, @Param("operadora") Operadora operadora);
}

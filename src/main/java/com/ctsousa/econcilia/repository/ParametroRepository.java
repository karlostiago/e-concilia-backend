package com.ctsousa.econcilia.repository;

import com.ctsousa.econcilia.enumaration.TipoParametro;
import com.ctsousa.econcilia.model.Parametro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametroRepository extends JpaRepository<Parametro, Long> {

    Parametro findByTipoParametro(@Param("tipoParametro") TipoParametro tipoParametro);
}

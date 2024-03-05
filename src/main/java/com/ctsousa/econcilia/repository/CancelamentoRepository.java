package com.ctsousa.econcilia.repository;

import com.ctsousa.econcilia.model.Cancelamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CancelamentoRepository extends JpaRepository<Cancelamento, Long> {

    @Query(value = "SELECT c FROM Cancelamento c WHERE c.comercianteId = :loja AND c.periodoId = :periodo")
    List<Cancelamento> buscar(@Param(value = "loja") String loja, @Param(value = "periodo") String periodo);
}

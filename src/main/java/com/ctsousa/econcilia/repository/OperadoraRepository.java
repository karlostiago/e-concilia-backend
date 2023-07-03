package com.ctsousa.econcilia.repository;

import com.ctsousa.econcilia.model.Operadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface OperadoraRepository extends JpaRepository<Operadora, Long> {

    boolean existsByDescricao(String descricao);

    @Query(value = "SELECT op FROM Operadora op INNER JOIN FETCH op.taxas WHERE op.id = :id")
    Operadora porID(Long id);

    @Query(value = "SELECT DISTINCT op FROM Operadora op INNER JOIN FETCH op.taxas WHERE op.descricao LIKE %:descricao%")
    List<Operadora> porDescricao(@Param(value = "descricao") String descricao);
}

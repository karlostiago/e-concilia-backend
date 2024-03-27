package com.ctsousa.econcilia.repository;

import com.ctsousa.econcilia.model.Consolidado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsolidadoRepository extends JpaRepository<Consolidado, Long> {

}

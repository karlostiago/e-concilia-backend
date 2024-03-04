package com.ctsousa.econcilia.repository;

import com.ctsousa.econcilia.model.Cancelamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CancelamentoRepository extends JpaRepository<Cancelamento, Long> {
}

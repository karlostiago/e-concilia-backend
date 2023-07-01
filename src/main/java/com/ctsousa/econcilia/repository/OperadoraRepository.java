package com.ctsousa.econcilia.repository;

import com.ctsousa.econcilia.model.Operadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperadoraRepository extends JpaRepository<Operadora, Long> {

}

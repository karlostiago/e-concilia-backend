package com.ctsousa.econcilia.repository;

import com.ctsousa.econcilia.model.AjusteVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AjusteVendaRepository extends JpaRepository<AjusteVenda, Long> {
}

package com.ctsousa.econcilia.repository;

import com.ctsousa.econcilia.model.IntegracaoBuffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntegracaoBufferRepository extends JpaRepository<IntegracaoBuffer, Long> {

    boolean existsByCnpj(String cnpj);
}

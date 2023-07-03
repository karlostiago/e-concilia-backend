package com.ctsousa.econcilia.repository;

import com.ctsousa.econcilia.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    boolean existsByCnpj(String cnpj);

    @Query("SELECT emp FROM Empresa emp WHERE emp.cnpj = :cnpj")
    Empresa porCnpj(@Param(value = "cnpj") final String cnpj);

    @Query("SELECT emp FROM Empresa emp WHERE emp.razaoSocial LIKE %:razaoSocial%")
    List<Empresa> porRazaoSocial( @Param(value = "razaoSocial") final String razaoSocial);
}
package com.ctsousa.econcilia.repository;

import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    @Query(value = "SELECT v FROM Venda v INNER JOIN FETCH v.cobranca INNER JOIN FETCH v.empresa emp INNER JOIN FETCH v.operadora oper INNER JOIN FETCH v.pagamento WHERE v.empresa = :empresa AND v.operadora = :operadora AND v.dataPedido BETWEEN :dtInicial AND :dtFinal")
    List<Venda> buscarPor(Empresa empresa, Operadora operadora, LocalDate dtInicial, LocalDate dtFinal);
}

package com.ctsousa.econcilia.repository;

import com.ctsousa.econcilia.enumaration.ImportacaoSituacao;
import com.ctsousa.econcilia.model.Importacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImportacaoRepository extends JpaRepository<Importacao, Long> {

    @Query("SELECT i FROM Importacao i WHERE i.situacao = :situacao")
    List<Importacao> buscarPorSituacaoAgendada(@Param(value = "situacao") final ImportacaoSituacao situacao);
}

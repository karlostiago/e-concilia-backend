package com.ctsousa.econcilia.repository;

import com.ctsousa.econcilia.enumaration.ImportacaoSituacao;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Importacao;
import com.ctsousa.econcilia.model.Operadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ImportacaoRepository extends JpaRepository<Importacao, Long> {

    @Query("SELECT i FROM Importacao i WHERE i.situacao = :situacao")
    List<Importacao> buscarPorSituacaoAgendada(@Param(value = "situacao") final ImportacaoSituacao situacao);

    @Query("SELECT import FROM Importacao import ORDER BY import.situacao")
    List<Importacao> pesquisarImportacoes();

    @Query("SELECT COUNT(i) > 0 FROM Importacao i WHERE i.situacao IN (:situacoes)")
    Boolean existsPorSituacao(@Param(value = "situacoes") List<ImportacaoSituacao> situacoes);
}

package com.ctsousa.econcilia.repository;

import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    @Query(value = "SELECT n FROM Notificacao n INNER JOIN FETCH n.empresa WHERE n.empresa IN (:empresas) AND n.lida = 0")
    List<Notificacao> naoLidas(@Param(value = "empresas") List<Empresa> empresas);

    @Query(value = "SELECT n FROM Notificacao n INNER JOIN FETCH n.empresa WHERE n.empresa IN (:empresas) AND n.resolvida = 0")
    List<Notificacao> naoResolvidas(@Param(value = "empresas") List<Empresa> empresas);
}

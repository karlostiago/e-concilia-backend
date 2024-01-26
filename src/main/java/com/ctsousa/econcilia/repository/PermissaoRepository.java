package com.ctsousa.econcilia.repository;

import com.ctsousa.econcilia.model.Permissao;
import com.ctsousa.econcilia.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Long> {

    @Query(value = "SELECT f.funcionalidade FROM permissao p INNER JOIN usuario u ON u.id = p.usuario_id INNER JOIN permissao_funcionalidade f ON f.permissao_id = p.id WHERE u.id = :usuarioId", nativeQuery = true)
    List<String> porUsuario(@Param("usuarioId") Long usuarioId);

    @Query(value = "SELECT p FROM Permissao p INNER JOIN FETCH p.funcionalidades f INNER JOIN FETCH p.usuario u WHERE p.usuario = :usuario ")
    Permissao porUsuario(@Param("usuario") Usuario usuario);

    Boolean existsByUsuario(Usuario usuario);

    @Query("SELECT p FROM Permissao p INNER JOIN FETCH p.funcionalidades f INNER JOIN FETCH p.usuario u WHERE p.id = :id")
    Permissao porId(@Param("id") Long id);

    @Query("SELECT p FROM Permissao p INNER JOIN FETCH p.funcionalidades f INNER JOIN FETCH p.usuario u WHERE 1 = 1")
    List<Permissao> todas();

    @Query(value = "SELECT p FROM Permissao p INNER JOIN FETCH p.funcionalidades f INNER JOIN FETCH p.usuario u WHERE u.nomeCompleto LIKE %:nomeCompleto% OR f LIKE %:permissao%")
    List<Permissao> pesquisar(@Param("nomeCompleto") String nomeCompleto, @Param("permissao") String permissao);
}

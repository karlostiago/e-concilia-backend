package com.ctsousa.econcilia.repository;

import com.ctsousa.econcilia.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query(value = "SELECT u FROM Usuario u WHERE u.id = :id")
    Optional<Usuario> porID(@Param(value = "id") Long id);

    @Query(value = "SELECT u FROM Usuario u WHERE u.nomeCompleto LIKE %:nomeCompleto%")
    List<Usuario> porNomeCompleto(@Param(value = "nomeCompleto") String nomeCompleto);

    @Query(value = "SELECT u FROM Usuario u WHERE u.email = :email")
    Usuario porEmail(@Param(value = "email") String email);

    boolean existsByEmail(String email);
}

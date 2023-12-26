package com.ctsousa.econcilia.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
public class UsuarioDTO implements Serializable {

    private Long id;

    @NotEmpty(message = "Campo nome completo é obrigatório.")
    private String nomeCompleto;

    @Email
    @NotEmpty(message = "Campo e-mail é obrigatório.")
    private String email;

    private String confirmaEmail;

    @NotEmpty(message = "Campo senha é obrigatório.")
    private String senha;

    private String confirmaSenha;
}
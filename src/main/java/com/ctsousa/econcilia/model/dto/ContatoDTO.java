package com.ctsousa.econcilia.model.dto;

import com.ctsousa.econcilia.Dto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
@Dto
public class ContatoDTO implements Serializable {

    @Email
    @NotEmpty(message = "Campo e-mail é obrigatório.")
    private String email;

    private String telefone;

    @NotEmpty(message = "Campo telefone celular é obrigatório.")
    private String celular;
}
package com.ctsousa.econcilia.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class EmpresaNaoEncontradaException extends RuntimeException {

    public EmpresaNaoEncontradaException(final Long id) {
        super(String.format("Não foi encontrada a empresa com o id '%d'.", id));
    }
}

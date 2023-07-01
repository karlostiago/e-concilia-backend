package com.ctsousa.econcilia.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class EmpresaExisteException extends RuntimeException {

    public EmpresaExisteException(final String cnpj) {
        super(String.format("Já existe uma empresa com o cnpj '%s'.", cnpj));
    }
}

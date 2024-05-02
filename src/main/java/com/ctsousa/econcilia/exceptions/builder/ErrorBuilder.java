package com.ctsousa.econcilia.exceptions.builder;

import com.ctsousa.econcilia.exceptions.Severidade;
import com.ctsousa.econcilia.exceptions.Error;
import org.springframework.http.HttpStatus;

public class ErrorBuilder {

    private static Error error;

    public ErrorBuilder() {
        error = new Error();
    }

    public static ErrorBuilder builder() {
        return new ErrorBuilder();
    }

    public ErrorBuilder comStatus(HttpStatus status) {
        error.setStatus(status);
        error.setCodigo(status.value());
        return this;
    }

    public ErrorBuilder comMensagem(String mensagem) {
        error.setMensagem(mensagem);
        return this;
    }

    public ErrorBuilder comDetalhe(Throwable ex) {
        error.setDetalhe(ex);
        return this;
    }

    public ErrorBuilder comSeveridade(Severidade severidade) {
        error.setSeveridade(severidade.name());
        return this;
    }

    public Error build() {
        return error;
    }
}

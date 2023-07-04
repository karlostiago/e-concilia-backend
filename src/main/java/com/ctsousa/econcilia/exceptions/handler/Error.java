package com.ctsousa.econcilia.exceptions.handler;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public class Error {

    @Setter
    @Getter
    private String mensagem;

    @Setter
    @Getter
    private Throwable detalhe;

    @Setter
    @Getter
    private HttpStatus status;

    @Setter
    @Getter
    private Integer codigo;

    @Getter
    private final LocalDate data = LocalDate.now();

    @Getter
    private final LocalTime hora = LocalTime.now();
}

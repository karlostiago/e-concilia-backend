package com.ctsousa.econcilia.exceptions.handler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class Error {

    @Setter
    private String mensagem;

    @Setter
    private Throwable detalhe;

    @Setter
    private HttpStatus status;

    @Setter
    private Integer codigo;

    private final LocalDate data = LocalDate.now();

    private final LocalTime hora = LocalTime.now();
}

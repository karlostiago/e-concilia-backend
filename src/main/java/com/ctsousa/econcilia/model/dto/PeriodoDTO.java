package com.ctsousa.econcilia.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class PeriodoDTO {
    private LocalDate de;
    private LocalDate ate;
}

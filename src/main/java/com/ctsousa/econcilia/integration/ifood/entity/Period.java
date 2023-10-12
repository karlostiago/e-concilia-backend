package com.ctsousa.econcilia.integration.ifood.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Period {

    private String periodId;

    private String merchantId;

    private String competence;

    private LocalDate expectedDate;

    private String status;
}

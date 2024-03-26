package com.ctsousa.econcilia.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DataSetDTO {

    private String label;

    private List<BigDecimal> data;
}

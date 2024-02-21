package com.ctsousa.econcilia.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class GraficoVendaMensalDTO {

    private List<String> labels;

    private List<DataSet> dataSets;

    @Data
    public static class DataSet {

        private String label;

        private List<BigDecimal> data;
    }
}

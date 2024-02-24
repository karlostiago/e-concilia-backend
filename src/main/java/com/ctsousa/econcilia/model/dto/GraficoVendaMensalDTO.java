package com.ctsousa.econcilia.model.dto;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ExcludedCoverage
public class GraficoVendaMensalDTO {

    private List<String> labels;

    private List<DataSetDTO> dataSets;

    @Data
    public static class DataSetDTO {

        private String label;

        private List<BigDecimal> data;
    }
}

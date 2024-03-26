package com.ctsousa.econcilia.model.dto;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ExcludedCoverage
public class GraficoVendaAnualDTO {

    private List<String> labels;

    private List<DataSetDTO> dataSets;
}

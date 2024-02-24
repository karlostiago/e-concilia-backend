package com.ctsousa.econcilia.model.dto;

import com.ctsousa.econcilia.Dto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Dto
public class TotalizadorDTO {

    private BigDecimal totalValorBruto = BigDecimal.valueOf(0D);

    private BigDecimal totalValorCancelado = BigDecimal.valueOf(0D);

    private BigDecimal totalValorPedido = BigDecimal.valueOf(0D);

    private BigDecimal totalValorLiquido = BigDecimal.valueOf(0D);
}

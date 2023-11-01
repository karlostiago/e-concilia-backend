package com.ctsousa.econcilia.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class Venda {

    private LocalDate dataPedido;

    private String pedidoId;

    private String periodoId;

    private LocalDate ultimaDataProcessamento;

    private String razaoSocial;

    private String numeroDocumento;

    private String modeloNegocio;

    private VendaPagamento pagamento;

    private Cobranca cobranca;

    private Boolean conciliado;

    private BigDecimal diferenca;

    public BigDecimal getValorBruto() {
        return this.cobranca.getValorParcial()
                .add(this.cobranca.getTaxaEntrega());
    }

    public BigDecimal getValorTotalPedido() {
        return this.cobranca.getValorBruto()
                .subtract(getValorDesconto())
                .add(this.cobranca.getTaxaServico())
                .add(getValorCancelado());
    }

    public BigDecimal getValorLiquido() {
        return this.cobranca.getValorBruto()
                .subtract(getValorDesconto())
                .subtract(this.cobranca.getValorCancelado())
                .add(this.cobranca.getComissao())
                .add(this.cobranca.getTaxaAdquirente());
    }

    public BigDecimal getValorDesconto() {
        if ("ifood".equalsIgnoreCase(this.pagamento.getResponsavel())) {
            return this.cobranca.getBeneficioOperadora()
                    .add(this.cobranca.getBeneficioComercio().multiply(BigDecimal.valueOf(-1D)));
        } else {
            return this.cobranca.getBeneficioOperadora();
        }
    }

    public BigDecimal getValorCancelado() {
        var valorCancelado = this.cobranca.getValorCancelado();
        if (this.cobranca.getValorCancelado().compareTo(BigDecimal.valueOf(0D)) < 0) {
            var taxa = this.cobranca.getBeneficioOperadora()
                    .add(this.cobranca.getTaxaAdquirente());

            valorCancelado = valorCancelado.add(taxa);
        }
        return valorCancelado;
    }
}

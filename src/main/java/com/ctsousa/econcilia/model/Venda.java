package com.ctsousa.econcilia.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "venda")
@EqualsAndHashCode(callSuper = false)
public class Venda extends Entidade {

    @Column(name = "data_pedido", nullable = false)
    private LocalDate dataPedido;

    @Column(name = "pedido_id", nullable = false)
    private String pedidoId;

    @Column(name = "periodo_id", nullable = false)
    private String periodoId;

    @Column(name = "ultima_data_processamento")
    private LocalDate ultimaDataProcessamento;

    @Column(name = "razao_social", nullable = false)
    private String razaoSocial;

    @Column(name = "numero_documento", nullable = false)
    private String numeroDocumento;

    @Column(name = "modelo_negocio", nullable = false)
    private String modeloNegocio;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "pagamento_id", nullable = false)
    private Pagamento pagamento;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "cobranca_id", nullable = false)
    private Cobranca cobranca;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "operadora_id", nullable = false)
    private Operadora operadora;

    @Column(name = "conciliado", nullable = false, columnDefinition = "boolean default false")
    private Boolean conciliado;

    @Column(name = "diferenca", nullable = false, columnDefinition = "decimal(19,2) default 0.0")
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

    @Override
    public String toString() {
        return "[PedidoId]:: " + pedidoId;
    }
}

package com.ctsousa.econcilia.model.ifood;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DetalhesPedido {

    @JsonProperty("benefits")
    private List<Beneficio> beneficios;

    @JsonProperty("orderType")
    private String tipoPedido;

    @JsonProperty("payments")
    private Pagamentos pagamentos;

    @JsonProperty("merchant")
    private Comerciante comerciante;

    @JsonProperty("salesChannel")
    private String canalVendas;

    @JsonProperty("picking")
    private Coleta coleta;

    @JsonProperty("orderTiming")
    private String timingPedido;

    @JsonProperty("createdAt")
    private String criadoEm;

    @JsonProperty("total")
    private Total total;

    @JsonProperty("preparationStartDateTime")
    private String inicioPreparo;

    @JsonProperty("id")
    private String id;

    @JsonProperty("displayId")
    private String idExibicao;

    @JsonProperty("items")
    private List<Item> itens;

    @JsonProperty("customer")
    private Cliente cliente;

    @JsonProperty("extraInfo")
    private String informacaoExtra;

    @JsonProperty("additionalFees")
    private List<TaxaAdicional> taxasAdicionais;

    @JsonProperty("delivery")
    private Entrega entrega;

    @JsonProperty("schedule")
    private Agenda agenda;

    @JsonProperty("indoor")
    private LocalInterno localInterno;

    @JsonProperty("takeout")
    private Retirada retirada;
}

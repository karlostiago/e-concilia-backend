package com.ctsousa.econcilia.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pagamento {

    private String tipo;

    private String metodo;

    private String bandeira;

    private String responsavel;

    private String numeroCartao;

    private String nsu;
}

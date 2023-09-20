package com.ctsousa.econcilia.model;

import com.ctsousa.econcilia.enumaration.Bandeira;
import com.ctsousa.econcilia.enumaration.MetodoPagamento;
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

    public void setBandeira(final String descBandeira) {
        Bandeira bandeira = Bandeira.porDescricao(descBandeira);
        this.bandeira = bandeira != null ? bandeira.getDescricao() : " - ";
    }

    public void setMetodo(final String descMetodoPagamento) {
        MetodoPagamento metodoPagamento = MetodoPagamento.porDescricao(descMetodoPagamento);
        this.metodo = metodoPagamento != null ? metodoPagamento.getDescricao() : " - ";
    }

    public void setTipo(String tipo) {
        this.tipo = tipo.equalsIgnoreCase("ONLINE") ? "Online" : "Offline";
    }
}

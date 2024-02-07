package com.ctsousa.econcilia.processor;

import com.ctsousa.econcilia.enumaration.FormaRecebimento;
import com.ctsousa.econcilia.model.Integracao;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProcessadorFiltro {

    private Integracao integracao;

    private String formaPagamento;

    private String cartaoBandeira;

    private FormaRecebimento formaRecebimento;

    private LocalDate dtInicial;

    private LocalDate dtFinal;
}

package com.ctsousa.econcilia.model.dto;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ExcludedCoverage
public class NotificacoesDTO {

    private List<NotificacaoDTO> notificacoes;

    private int quantidade;

    public int getQuantidade() {
        return notificacoes == null ? 0 : notificacoes.size();
    }
}

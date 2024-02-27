package com.ctsousa.econcilia.model.dto;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@ExcludedCoverage
public class NotificacaoDTO {

    private List<MensagemDTO> mensagens;

    public int getQuantidade() {
        return mensagens == null ? 0 : mensagens.size();
    }
}

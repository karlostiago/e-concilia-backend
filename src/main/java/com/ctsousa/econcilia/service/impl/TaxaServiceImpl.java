package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Taxa;
import com.ctsousa.econcilia.model.dto.TaxaDTO;
import com.ctsousa.econcilia.service.TaxaService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TaxaServiceImpl implements TaxaService {
    @Override
    public void validar(TaxaDTO taxaDTO) {
        if (taxaDTO.getEntraEmVigor().isAfter(taxaDTO.getValidoAte())) {
            throw new NotificacaoException("O campo entrar em vigor não pode ser maior que o campo válido até.");
        }
    }

    @Override
    public Long calcularTempoExpiracao(LocalDate dataInicial, LocalDate dataFinal) {
        Taxa taxa = new Taxa();
        taxa.setEntraEmVigor(dataInicial);
        taxa.setValidoAte(dataFinal);
        return taxa.expiraEm();
    }

    @Override
    public void verificaDuplicidade(List<Taxa> taxas) {
        Map<String, Taxa> unique = new HashMap<>();
        for (Taxa taxa : taxas) {
            if (unique.containsKey(taxa.getDescricao())) {
                throw new NotificacaoException("Não é permitido duplicar taxas.");
            }
            unique.put(taxa.getDescricao(), taxa);
        }
    }

    @Override
    public void validaEntraEmVigor(List<Taxa> taxas) {
        for (Taxa taxa : taxas) {
            if (taxa.getEntraEmVigor().isAfter(taxa.getValidoAte())) {
                throw new NotificacaoException("A taxa não pode entrar em vigor, pois está maior que o período de validade.");
            }
        }
    }
}

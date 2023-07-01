package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Taxa;
import com.ctsousa.econcilia.repository.OperadoraRepository;
import com.ctsousa.econcilia.service.OperadoraService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OperadoraServiceImpl implements OperadoraService  {
    private final OperadoraRepository operadoraRepository;
    public OperadoraServiceImpl(OperadoraRepository operadoraRepository) {
        this.operadoraRepository = operadoraRepository;
    }
    @Override
    public Operadora salvar (Operadora operadora) {
        validar(operadora);
        temTaxaDuplicada(operadora.getTaxas());
        podeEntrarEmVigor(operadora.getTaxas());
        return operadoraRepository.save(operadora);
    }
    private void validar (final Operadora operadora) {
        if (operadora.getTaxas().isEmpty()) {
            throw new NotificacaoException("Informe pelo menos uma taxa.");
        }
    }
    private void temTaxaDuplicada(final List<Taxa> taxas) {
        Map<String, Taxa> unique = new HashMap<>();
        for (Taxa taxa : taxas) {
            if (unique.containsKey(taxa.getDescricao())) {
                throw new NotificacaoException("Não é permitido duplicar taxas.");
            }
            unique.put(taxa.getDescricao(), taxa);
        }
    }
    private void podeEntrarEmVigor(final List<Taxa> taxas) {
        for (Taxa taxa : taxas) {
            if (taxa.getEntraEmVigor().isAfter(taxa.getValidoAte())) {
                throw new NotificacaoException("A taxa não pode entrar em vigor, pois está maior que o período de validade.");
            }
            if (taxa.getEntraEmVigor().isBefore(LocalDate.now())) {
                throw new NotificacaoException("A taxa não pode entrar em vigor, pois está menor que o período atual/data atual.");
            }
            if (taxa.getValidoAte().isAfter(taxa.getEntraEmVigor())) {
                throw new NotificacaoException("A taxa não pode entrar em vigor, o período de validade está maior.");
            }
        }
    }
}

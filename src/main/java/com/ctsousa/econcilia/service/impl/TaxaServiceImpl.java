package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Taxa;
import com.ctsousa.econcilia.repository.TaxaRepository;
import com.ctsousa.econcilia.service.TaxaService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ctsousa.econcilia.util.DataUtil.paraPtBr;
import static com.ctsousa.econcilia.util.DecimalUtil.monetarioPtBr;

@Component
public class TaxaServiceImpl implements TaxaService {

    private final TaxaRepository taxaRepository;

    public TaxaServiceImpl(TaxaRepository taxaRepository) {
        this.taxaRepository = taxaRepository;
    }

    @Override
    public Taxa buscarPorDescricaoEmpresa(String descricao, Empresa empresa) {
        Optional<Taxa> taxaOpt = taxaRepository.por(descricao, empresa.getId());

        if (taxaOpt.isEmpty()) {
            throw new NotificacaoException("Nenhuma taxa encontrada com descrição :: " + descricao + ", e empresa :: " + empresa.getRazaoSocial());
        }

        return taxaOpt.get();
    }

    @Override
    public void validar(Taxa taxa) {
        if (taxa.getEntraEmVigor().isBefore(LocalDate.now())) {
            throw new NotificacaoException("O campo entrar em vigor não pode ser menor que a data atual.");
        }

        if (taxa.getEntraEmVigor().isAfter(taxa.getValidoAte())) {
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

    @Override
    public List<Taxa> buscarPorContrato(final Long contratoId) {
        List<Taxa> taxas = taxaRepository.findByContrato(contratoId);

        if (taxas.isEmpty()) {
            throw new NotificacaoException("Não foi encontrada nenhuma taxa para o contrato de número " + contratoId);
        }

        return taxas;
    }

    @Override
    public List<Taxa> buscarPorOperadora(Long operadoraId) {
        List<Taxa> taxas = taxaRepository.findByOperadora(operadoraId);

        if (taxas.isEmpty()) {
            throw new NotificacaoException("Não foi encontrada nenhuma taxa para o operadora com id: " + operadoraId);
        }

        return taxas;
    }

    @Override
    public List<Taxa> buscarPorEmpresa(Long empresaId) {
        List<Taxa> taxas = taxaRepository.findByEmpresa(empresaId);

        if (taxas.isEmpty()) {
            throw new NotificacaoException("Não foi encontrada nenhuma taxa para a empresa " + empresaId);
        }

        return taxas;
    }

    @Override
    public List<Taxa> buscarTodos() {
        List<Taxa> taxas = taxaRepository.findByAll();

        if (taxas.isEmpty()) {
            throw new NotificacaoException("Não foi encontrada nenhuma taxa");
        }

        return taxas;
    }

    @Override
    public Taxa pesquisarPorId(Long id) {
        return taxaRepository.porId(id)
                .orElseThrow(() -> new NotificacaoException(String.format("Taxa com id %d não encontrado", id)));
    }

    @Override
    public Taxa ativar(Long id) {
        Taxa taxa = this.pesquisarPorId(id);
        taxa.setAtivo(true);
        taxaRepository.save(taxa);

        return taxa;
    }

    @Override
    public Taxa desativar(Long id) {
        Taxa taxa = this.pesquisarPorId(id);
        taxa.setAtivo(false);
        taxaRepository.save(taxa);

        return taxa;
    }

    @Override
    public Taxa buscarPor(Empresa empresa, Operadora operadora, String descricao, BigDecimal valor) {
        return taxaRepository.por(empresa.getId(), operadora.getId(), descricao, valor)
                .orElseThrow(() -> new NotificacaoException(String.format("Nenhuma taxa com empresa id %d e operadora id %d encontrado", empresa.getId(), operadora.getId())));
    }

    @Override
    public byte [] gerarCSV(LocalDate dataInicial, LocalDate dataFinal, Empresa empresa, Operadora operadora) {
        List<Taxa> taxas = taxaRepository.por(dataInicial, dataFinal, empresa.getId(), operadora.getId());

        if (taxas.isEmpty()) return new byte[0];

        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Descrição;Valor;Data inicio;Data final;Tipo\n");

        for (Taxa taxa : taxas) {
            csvBuilder.append(taxa.getDescricao()).append(";")
                    .append(monetarioPtBr(taxa.getValor())).append(";")
                    .append(paraPtBr(taxa.getEntraEmVigor())).append(";")
                    .append(paraPtBr(taxa.getValidoAte())).append(";")
                    .append(taxa.getTipo().name()).append("\n");
        }

        return csvBuilder.toString().getBytes();
    }
}

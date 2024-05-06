package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Parametro;
import com.ctsousa.econcilia.model.dto.ParametroDTO;
import com.ctsousa.econcilia.repository.ParametroRepository;
import com.ctsousa.econcilia.service.ParametroService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ParametroServiceImpl implements ParametroService {

    private final ParametroRepository parametroRepository;

    public ParametroServiceImpl(ParametroRepository parametroRepository) {
        this.parametroRepository = parametroRepository;
    }

    @Override
    public List<Parametro> pesquisar(Empresa empresa, Operadora operadora) {

        if ((empresa == null || empresa.getId() == null)
                || (operadora == null || operadora.getId() == null)) {
            return List.of();
        }

        return parametroRepository.buscaParametroEmpresaOperadora(empresa, operadora);
    }

    @Override
    public Parametro salvar(Parametro parametro) {
        if (parametro.getEmpresa() == null || parametro.getEmpresa().getId() == null) {
            throw new NotificacaoException("Selecione uma empresa.");
        }

        if (parametro.getOperadora() == null || parametro.getOperadora().getId() == null) {
            throw new NotificacaoException("Selecione uma operadora.");
        }

        Empresa empresa = parametro.getEmpresa();
        Operadora operadora = parametro.getOperadora();
        Parametro parametroEncontrado = parametroRepository.buscaParametroTipoEmpresaOperadora(parametro.getTipoParametro(), parametro.getEmpresa(), parametro.getOperadora());

        if (parametroEncontrado != null) {
            parametro.setId(parametroEncontrado.getId());
        }

        parametro = parametroRepository.save(parametro);
        parametro.setEmpresa(empresa);
        parametro.setOperadora(operadora);

        return parametro;
    }
}

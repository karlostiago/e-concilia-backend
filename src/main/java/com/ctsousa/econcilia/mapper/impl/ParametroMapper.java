package com.ctsousa.econcilia.mapper.impl;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import com.ctsousa.econcilia.enumaration.TipoParametro;
import com.ctsousa.econcilia.integration.ifood.entity.SaleAdjustment;
import com.ctsousa.econcilia.mapper.ColecaoMapper;
import com.ctsousa.econcilia.mapper.DtoMapper;
import com.ctsousa.econcilia.mapper.EntidadeMapper;
import com.ctsousa.econcilia.model.*;
import com.ctsousa.econcilia.model.dto.ParametroDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Component
@ExcludedCoverage
public class ParametroMapper implements DtoMapper<Parametro, ParametroDTO>, ColecaoMapper<Parametro, ParametroDTO> {

    @Override
    public List<ParametroDTO> paraLista(List<Parametro> parametros) {
        return parametros.stream()
                .map(this::paraDTO)
                .toList();
    }

    public Parametro paraEntidade(ParametroDTO parametroDTO, Empresa empresa, Operadora operadora) {
        Parametro parametro = new Parametro();
        parametro.setEmpresa(empresa);
        parametro.setOperadora(operadora);
        parametro.setTipoParametro(TipoParametro.por(parametroDTO.getTipoParametro()));
        parametro.setAtivo(parametroDTO.getAtivo());
        return parametro;
    }

    @Override
    public ParametroDTO paraDTO(Parametro parametro) {
        ParametroDTO parametroDTO = new ParametroDTO();
        parametroDTO.setTipoParametro(parametro.getTipoParametro().name());
        parametroDTO.setDescricao(parametro.getTipoParametro().getDescricao());
        parametroDTO.setPreFixo(parametro.getTipoParametro().getPreFixo());
        parametroDTO.setEmpresa(parametro.getEmpresa().getRazaoSocial());
        parametroDTO.setOperadora(parametro.getOperadora().getDescricao());
        parametroDTO.setAtivo(parametro.isAtivo());
        return parametroDTO;
    }

    public List<ParametroDTO> paraLista(TipoParametro [] tipoParametro, String empresa, String operadora) {
        List<ParametroDTO> parametros = new ArrayList<>();

        List<TipoParametro> tiposDeParametro = Arrays.stream(tipoParametro)
                .sorted(Comparator.comparing(Enum::ordinal))
                .toList();

        for (TipoParametro t : tiposDeParametro) {
            ParametroDTO parametroDTO = new ParametroDTO();
            parametroDTO.setTipoParametro(t.name());
            parametroDTO.setPreFixo(t.getPreFixo());
            parametroDTO.setDescricao(t.getDescricao());
            parametroDTO.setEmpresa(empresa);
            parametroDTO.setOperadora(operadora);
            parametros.add(parametroDTO);
        }

        return parametros;
    }

    public List<ParametroDTO> substituir(List<Parametro> parametros, List<ParametroDTO> parametroDTOS) {
        for (Parametro parametro : parametros) {
            int posicao = 0;
            for (ParametroDTO parametroDTO : parametroDTOS) {
                if (parametro.getTipoParametro().equals(TipoParametro.por(parametroDTO.getTipoParametro()))) {
                    parametroDTOS.set(posicao, paraDTO(parametro));
                    break;
                }
                posicao++;
            }
        }
        return parametroDTOS;
    }
}

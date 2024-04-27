package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.enumaration.TipoRelatorio;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.dto.RelatorioDTO;
import com.ctsousa.econcilia.service.RelatorioService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RelatorioServiceImpl implements RelatorioService {

    @Override
    public RelatorioDTO gerarDados(TipoRelatorio tipoRelatorio, JpaRepository<?, ?> repository, LocalDate dataInicial, LocalDate dataFinal, Empresa empresa, Operadora operadora) {
        return tipoRelatorio.gerarDados(repository, dataInicial, dataFinal, empresa, operadora);
    }
}

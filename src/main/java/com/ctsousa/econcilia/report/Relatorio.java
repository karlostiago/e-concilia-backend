package com.ctsousa.econcilia.report;

import com.ctsousa.econcilia.enumaration.TipoRelatorio;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.report.dto.RelatorioDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface Relatorio {

    RelatorioDTO gerarDados(TipoRelatorio tipoRelatorio, JpaRepository<?, ?> repository, LocalDate dataInicial, LocalDate dataFinal, Empresa empresa, Operadora operadora);
}

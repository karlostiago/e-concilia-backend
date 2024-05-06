package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Parametro;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ParametroService {

    Parametro salvar(Parametro parametro);

    List<Parametro> pesquisar(Empresa empresa, Operadora operadora);
}

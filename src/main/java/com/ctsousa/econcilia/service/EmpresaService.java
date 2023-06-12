package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Empresa;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmpresaService {

    Empresa salvar (final Empresa empresa);

    List<Empresa> pesquisar (String razaoSocial, String cnpj);

    void deletar (Long id);
}

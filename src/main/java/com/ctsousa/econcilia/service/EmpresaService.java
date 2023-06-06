package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Empresa;
import org.springframework.stereotype.Service;

@Service
public interface EmpresaService {

    Empresa salvar (final Empresa empresa);
}

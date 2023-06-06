package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.repository.EmpresaRepository;
import com.ctsousa.econcilia.service.EmpresaService;
import org.springframework.stereotype.Component;

@Component
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;

    EmpresaServiceImpl(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Override
    public Empresa salvar(final Empresa empresa) {
        return empresaRepository.save(empresa);
    }
}

package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.dto.EmpresaDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmpresaService {
    Empresa salvar (final Empresa empresa);

    List<Empresa> pesquisar (String razaoSocial, String cnpj);

    void deletar (Long id);

    Empresa pesquisarPorId (Long id);

    Empresa atualizar (Long id, EmpresaDTO empresaDTO);

    Empresa ativar (Long id);

    Empresa desativar (Long id);
}
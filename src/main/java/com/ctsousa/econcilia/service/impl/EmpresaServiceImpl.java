package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.dto.EmpresaDTO;
import com.ctsousa.econcilia.repository.EmpresaRepository;
import com.ctsousa.econcilia.service.EmpresaService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static com.ctsousa.econcilia.util.StringUtil.somenteNumero;

@Component
public class EmpresaServiceImpl implements EmpresaService {
    private final EmpresaRepository empresaRepository;

    EmpresaServiceImpl(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Override
    public Empresa salvar(final Empresa empresa) {
        if (empresaRepository.existsByCnpj(somenteNumero(empresa.getCnpj()))) {
            throw new NotificacaoException(String.format("Já existe uma empresa com o cnpj %s cadastrado.", empresa.getCnpj()));
        }

        return empresaRepository.save(empresa);
    }

    @Override
    public List<Empresa> pesquisar(final String razaoSocial, final String cnpj) {
        if (null != razaoSocial && !razaoSocial.isEmpty()) {
            return empresaRepository.porRazaoSocial(razaoSocial);
        }

        if (null != cnpj && !cnpj.isEmpty()) {
            return Collections.singletonList(empresaRepository.porCnpj(somenteNumero(cnpj)));
        }

        return empresaRepository.findAll();
    }

    @Override
    public void deletar(Long id) {
        var empresa = pesquisarPorId(id);
        empresaRepository.delete(empresa);
    }

    @Override
    public Empresa pesquisarPorId(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new NotificacaoException(String.format("Empresa com id %d não encontrado", id)));
    }

    @Override
    public Empresa atualizar(Long id, EmpresaDTO empresaDTO) {
        Empresa empresa = pesquisarPorId(id);
        BeanUtils.copyProperties(empresaDTO, empresa, "id");
        return empresaRepository.save(empresa);
    }

    @Override
    public Empresa ativar(Long id) {
        Empresa empresa = this.pesquisarPorId(id);
        empresa.setAtivo(true);
        empresaRepository.save(empresa);
        return empresa;
    }

    @Override
    public Empresa desativar(Long id) {
        Empresa empresa = this.pesquisarPorId(id);
        empresa.setAtivo(false);
        empresaRepository.save(empresa);
        return empresa;
    }
}